package pl.put.poznan.scenario_quality_checker.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NewScenarioParser {

    private static List<Step> parseSteps(List<?> steps) {
        return steps.stream()
                .map(NewScenarioParser::parseStep)
                .collect(Collectors.toList());
    }

    private static Step parseStep(Object step) {
        if (step instanceof SimpleStep) { // Taki step już istnieje jako obiekt SimpleStep
            return (SimpleStep) step;
        } else if (step instanceof String) { //Tworzy nowego SimpleStepa
            return new SimpleStep((String) step);
        } else if (step instanceof Map) { // Natknięcie się na {} w steps
            Map<?, ?> stepMap = (Map<?, ?>) step;

            if (stepMap.containsKey("IF")) { //Tworzenie ConditionalStepu IFa
                String condition = (String) stepMap.get("IF");
                List<Step> innerSteps = parseSteps((List<?>) stepMap.get("steps"));
                return new ConditionalStep(ConditionalStep.ConditionalType.IF, condition, innerSteps);
            } else if (stepMap.containsKey("FOR EACH")) { //Tworzenie IterativeStepu FOR EACHa
                String loopVariable = (String) stepMap.get("FOR EACH");
                List<Step> innerSteps = parseSteps((List<?>) stepMap.get("steps"));
                return new IterativeStep(loopVariable, innerSteps);
            } else if (stepMap.containsKey("ELSE")) { //Tworzenie ConditionalStepu ELSa
                String loopVariable = (String) stepMap.get("ELSE");
                List<Step> innerSteps = parseSteps((List<?>) stepMap.get("steps"));
                return new ConditionalStep(ConditionalStep.ConditionalType.ELSE, loopVariable, innerSteps);
            }
        }

        // Obsługa błędnego formatu JSON
        throw new IllegalArgumentException("Nieoczekiwany format kroku: " + step);
    }

    public static List<String> parseScenarioFromString2(String jsonContent) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> rawData = mapper.readValue(jsonContent, Map.class);
        return createScenarioFromData2(rawData);
    }

    private static List<String> createScenarioFromData2(Map<String, Object> rawData) {
        List<String> outputLogs = new ArrayList<>();

        Scenario scenario = new Scenario();
        scenario.setTitle((String) rawData.get("title"));

        Map<String, List<String>> rawActors = (Map<String, List<String>>) rawData.get("actors");
        List<Actor> externalActors = rawActors.get("external").stream()
                .map(actor -> new Actor(Actor.ActorType.EXTERNAL, actor))
                .collect(Collectors.toList());

        List<Actor> systemActors = rawActors.get("system").stream()
                .map(actor -> new Actor(Actor.ActorType.SYSTEM, actor))
                .collect(Collectors.toList());

        scenario.setExternalActors(externalActors);
        scenario.setSystemActors(systemActors);

        List<?> rawSteps = (List<?>) rawData.get("steps");
        List<Step> parsedSteps = parseSteps(rawSteps);
        scenario.setSteps(parsedSteps);

        outputLogs.add("Tytuł: " + scenario.getTitle());
        outputLogs.add("Aktorzy: " + String.join(", ", parseActors2(scenario.getExternalActors())));
        outputLogs.add("Aktor systemowy: " + String.join(", ", parseActors2(scenario.getSystemActors())));

        // Printowanie kroków kolejno
        printSteps2(scenario.getSteps(), "", outputLogs);

        // Użycie wizytatora - liczenie głównych kroków
        ScenarioStepCounter mainStepCounter = new ScenarioStepCounter(false);
        for (Step step : scenario.getSteps()) {
            step.accept(mainStepCounter);
        }
        outputLogs.add("Liczba głównych kroków: " + mainStepCounter.getStepCount());

        // Użycie wizytatora - liczenie wszystkich kroków, w tym podkroków
        ScenarioStepCounter allStepCounter = new ScenarioStepCounter(true);
        for (Step step : scenario.getSteps()) {
            step.accept(allStepCounter);
        }
        outputLogs.add("Liczba wszystkich kroków (w tym podkroków): " + allStepCounter.getStepCount());

        ConditionalStepCounter conditionCounter = new ConditionalStepCounter();
        outputLogs.add("Liczba wszystkich kroków warunkowych: " + conditionCounter.countConditionalSteps(scenario));

        // Użycie wizytatora - walidacja kroków bez aktora
        List<String> invalidSteps = StepActorValidator.findStepsWithoutActors(scenario);
        if (invalidSteps.isEmpty()) {
            outputLogs.add("Wszystkie kroki zaczynają się od aktora.");
        } else {
            outputLogs.add("Kroki bez aktora:");
            outputLogs.addAll(invalidSteps);
        }

        return outputLogs;
    }

    private static void printSteps2(List<Step> steps, String prefix, List<String> outputLogs) {
        if (steps == null) return;

        int stepCounter = 1;

        for (Step step : steps) {
            String currentPrefix = prefix.isEmpty() ? String.valueOf(stepCounter) : prefix + "." + stepCounter;

            if (step instanceof ConditionalStep) {
                ConditionalStep condStep = (ConditionalStep) step;
                outputLogs.add(currentPrefix + ". " + condStep.getConditionalType() + ": " + condStep.getCondition());
                printSteps2(condStep.getSteps(), "\t" + currentPrefix, outputLogs);
            } else if (step instanceof IterativeStep) {
                IterativeStep iterStep = (IterativeStep) step;
                outputLogs.add(currentPrefix + ". FOR EACH: " + iterStep.getLoopVariable());
                printSteps2(iterStep.getSteps(), "\t" + currentPrefix, outputLogs);
            } else if (step instanceof SimpleStep) {
                outputLogs.add(currentPrefix + ". " + ((SimpleStep) step).getDescription());
            }
            stepCounter++;
        }
    }

    private static List<String> parseActors2(List<Actor> actors) {
        return actors.stream().map(Actor::getName).collect(Collectors.toList());
    }


}