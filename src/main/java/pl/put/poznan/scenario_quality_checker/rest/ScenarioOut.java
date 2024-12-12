package pl.put.poznan.scenario_quality_checker.rest;

import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ScenarioOut {

    @GetMapping("/parse-scenario")
    public List<String> getParsedScenario(@RequestParam String filePath) {
        List<String> outputLogs = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Wczytanie JSONa
            Map<String, Object> rawData = mapper.readValue(new File(filePath), Map.class);

            Scenario scenario = new Scenario();
            scenario.setTitle((String) rawData.get("title"));

            Map<String, List<String>> rawActors = (Map<String, List<String>>) rawData.get("actors");
            Actors actors = new Actors();
            actors.setExternal(rawActors.get("external"));
            actors.setSystem(rawActors.get("system"));
            scenario.setActors(actors);

            // Parsowanie kroków
            List<?> rawSteps = (List<?>) rawData.get("steps");
            List<SimpleStep> parsedSteps = parseSteps(rawSteps);
            scenario.setSteps(parsedSteps);

            outputLogs.add("Tytuł: " + scenario.getTitle());
            outputLogs.add("Aktorzy: " + String.join(", ", scenario.getActors().getExternal()));
            outputLogs.add("Aktor systemowy: " + String.join(", ", scenario.getActors().getSystem()));
            outputLogs.add("");

            // Printowanie kroków kolejno
            printSteps(scenario.getSteps(), "", outputLogs);

        } catch (IOException e) {
            outputLogs.add("Błąd podczas przetwarzania pliku: " + e.getMessage());
        }
        return outputLogs;
    }

    /**
     * Funkcja przetwarzająca listę kroków.
     */
    private static List<SimpleStep> parseSteps(List<?> steps) {
        return steps.stream()
                .map(ScenarioOut::parseStep)
                .collect(Collectors.toList());
    }

    /**
     * Funkcja przetwarzająca pojedynczy krok.
     */
    private static SimpleStep parseStep(Object step) {
        if (step instanceof SimpleStep) {
            return (SimpleStep) step;
        } else if (step instanceof String) {
            return new SimpleStep((String) step);
        } else if (step instanceof Map) {
            Map<?, ?> stepMap = (Map<?, ?>) step;

            if (stepMap.containsKey("IF")) {
                String condition = (String) stepMap.get("IF");
                List<SimpleStep> innerSteps = parseSteps((List<?>) stepMap.get("steps"));
                return new ConditionalStep("IF: " + condition, condition, innerSteps);
            } else if (stepMap.containsKey("FOR EACH")) {
                String loopVariable = (String) stepMap.get("FOR EACH");
                List<SimpleStep> innerSteps = parseSteps((List<?>) stepMap.get("steps"));
                return new IterativeStep("FOR EACH: " + loopVariable, loopVariable, innerSteps);
            } else if (stepMap.containsKey("ELSE")) {
                String loopVariable = (String) stepMap.get("ELSE");
                List<SimpleStep> innerSteps = parseSteps((List<?>) stepMap.get("steps"));
                return new ConditionalStep("ELSE: " + loopVariable, loopVariable, innerSteps);
            }
        }
        throw new IllegalArgumentException("Nieoczekiwany format kroku: " + step);
    }

    /**
     * Funkcja printująca tak jak w przykładzie
     */
    private static void printSteps(List<SimpleStep> steps, String prefix, List<String> outputLogs) {
        if (steps == null) return;

        int stepCounter = 1;

        for (SimpleStep step : steps) {
            String currentPrefix = prefix.isEmpty() ? String.valueOf(stepCounter) : prefix + "." + stepCounter;

            if (step instanceof ConditionalStep) {
                ConditionalStep condStep = (ConditionalStep) step;
                outputLogs.add(currentPrefix + ". " + condStep.getDescription());
                printSteps(condStep.getSteps(), "\t" + currentPrefix, outputLogs);
            } else if (step instanceof IterativeStep) {
                IterativeStep iterStep = (IterativeStep) step;
                outputLogs.add(currentPrefix + ". " + iterStep.getDescription());
                printSteps(iterStep.getSteps(), "\t" + currentPrefix, outputLogs);
            } else {
                outputLogs.add(currentPrefix + ". " + step.getDescription());
            }
            stepCounter++;
        }
    }
}
