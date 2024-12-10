package pl.put.poznan.scenario_quality_checker.logic;

import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScenarioParser {

    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Wczytanie JSONa
            Map<String, Object> rawData = mapper.readValue(new File("InputData/sample_scenario_with_else.json"), Map.class);

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

            System.out.println("Tytuł: " + scenario.getTitle());
            System.out.println("Aktorzy: " + String.join(", ", scenario.getActors().getExternal()));
            System.out.println("Aktor systemowy: " + String.join(", ", scenario.getActors().getSystem()));
            System.out.println();

            // Printowanie kroków kolejno
            printSteps(scenario.getSteps(), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Funkcja przetwarzająca listę kroków.
     */
    private static List<SimpleStep> parseSteps(List<?> steps) {
        return steps.stream()
                .map(ScenarioParser::parseStep)
                .collect(Collectors.toList());
    }


    /**
     * Funkcja przetwarzająca pojedynczy krok.
     */
    private static SimpleStep parseStep(Object step) {
        if (step instanceof SimpleStep) { // Taki step już istnieje jako obiekt SimpleStep
            return (SimpleStep) step;
        } else if (step instanceof String) { //Tworzy nowego SimpleStepa
            return new SimpleStep((String) step);
        } else if (step instanceof Map) { // Natknięcie się na {} w steps
            Map<?, ?> stepMap = (Map<?, ?>) step;

            if (stepMap.containsKey("IF")) { //Tworzenie ConditionalStepu IFa
                String condition = (String) stepMap.get("IF");
                List<SimpleStep> innerSteps = parseSteps((List<?>) stepMap.get("steps"));
                return new ConditionalStep("IF: " + condition, condition, innerSteps);
            } else if (stepMap.containsKey("FOR EACH")) { //Tworzenie IterativeStepu FOR EACHa
                String loopVariable = (String) stepMap.get("FOR EACH");
                List<SimpleStep> innerSteps = parseSteps((List<?>) stepMap.get("steps"));
                return new IterativeStep("FOR EACH: " + loopVariable, loopVariable, innerSteps);
            } else if (stepMap.containsKey("ELSE")) { //Tworzenie ConditionalStepu ELSa
                String loopVariable = (String) stepMap.get("ELSE");
                List<SimpleStep> innerSteps = parseSteps((List<?>) stepMap.get("steps"));
                return new ConditionalStep("ELSE: " + loopVariable, loopVariable, innerSteps);
            }
        }

        // Obsługa błędnego formatu JSON
        throw new IllegalArgumentException("Nieoczekiwany format kroku: " + step);
    }


    /**
     * Funkcja printująca tak jak w przykładzie
     */
    private static void printSteps(List<SimpleStep> steps, String prefix) {
        if (steps == null) return;

        int stepCounter = 1;

        for (SimpleStep step : steps) {
            String currentPrefix = prefix.isEmpty() ? String.valueOf(stepCounter) : prefix + "." + stepCounter;

            if (step instanceof ConditionalStep) {
                ConditionalStep condStep = (ConditionalStep) step;
                System.out.println(currentPrefix + ". " + condStep.getDescription());
                printSteps(condStep.getSteps(), "\t" + currentPrefix); // Ciągnięcie dalej tego ConditionalStepów
            } else if (step instanceof IterativeStep) {
                IterativeStep iterStep = (IterativeStep) step;
                System.out.println(currentPrefix + ". " + iterStep.getDescription());
                printSteps(iterStep.getSteps(), "\t" + currentPrefix); // Ciągnięcie dalej tego IterativeStepów
            } else {
                System.out.println(currentPrefix + ". " + step.getDescription()); // Zwykły Stepik B)
            }
            stepCounter++;
        }
    }

}