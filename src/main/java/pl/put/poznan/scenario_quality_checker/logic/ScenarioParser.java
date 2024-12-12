package pl.put.poznan.scenario_quality_checker.logic;

import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScenarioParser {

    public static Scenario parseScenarioFromFile(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Wczytanie JSONa
        Map<String, Object> rawData = mapper.readValue(new File(path), Map.class);

        Scenario scenario = new Scenario();
        scenario.setTitle((String) rawData.get("title"));

        Map<String, List<String>> rawActors = (Map<String, List<String>>) rawData.get("actors");
        List<Actor> externalActors = new java.util.ArrayList<>(List.of());
        List<Actor> systemActors = new java.util.ArrayList<>(List.of());
        for (String rawExternalActor : rawActors.get("external")) {
            externalActors.add(new Actor(Actor.ActorType.EXTERNAL, rawExternalActor));
        }
        for (String rawSystemActor : rawActors.get("system")) {
            systemActors.add(new Actor(Actor.ActorType.EXTERNAL, rawSystemActor));
        }
        scenario.setExternalActors(externalActors);
        scenario.setSystemActors(systemActors);

        // Parsowanie kroków
        List<?> rawSteps = (List<?>) rawData.get("steps");
        List<SimpleStep> parsedSteps = parseSteps(rawSteps);
        scenario.setSteps(parsedSteps);

        System.out.println("Tytuł: " + scenario.getTitle());
        System.out.println("Aktorzy: " + String.join(", ", parseActors(scenario.getExternalActors())));
        System.out.println("Aktor systemowy: " + String.join(", ", parseActors(scenario.getSystemActors())));
        System.out.println();

        // Printowanie kroków kolejno
        printSteps(scenario.getSteps(), "");

        // Użycie wizytatora - liczenie głównych kroków
        ScenarioStepCounter mainStepCounter = new ScenarioStepCounter(false);
        for (SimpleStep step : scenario.getSteps()) {
            step.accept(mainStepCounter);
        }
        System.out.println("Liczba głównych kroków: " + mainStepCounter.getStepCount());

        // Użycie wizytatora - liczenie wszystkich kroków, w tym podkroków
        ScenarioStepCounter allStepCounter = new ScenarioStepCounter(true);
        for (SimpleStep step : scenario.getSteps()) {
            step.accept(allStepCounter);
        }
        System.out.println("Liczba wszystkich kroków (w tym podkroków): " + allStepCounter.getStepCount());
        
        ConditionalStepCounter conditionCunter = new ConditionalStepCounter();

        System.out.println("Liczba wszystkich kroków warunkowych: " + conditionCunter.countConditionalSteps(scenario));
        
        // Użycie wizytatora - walidacja kroków bez aktora
        List<String> invalidSteps = StepActorValidator.findStepsWithoutActors(scenario);
        if (invalidSteps.isEmpty()) {
            System.out.println("Wszystkie kroki zaczynają się od aktora.");
        } else {
            System.out.println("Kroki bez aktora:");
            invalidSteps.forEach(System.out::println);
        }
        
        return scenario;
    }


    /**
     * Funkcja przetwarzająca listę aktorów
     */
    private static List<String> parseActors(List<Actor> actors) {
        return actors.stream().map(Actor::getName).collect(Collectors.toList());
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

    public static Scenario parseScenarioFromString(String jsonContent) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> rawData = mapper.readValue(jsonContent, Map.class);
        return createScenarioFromData(rawData);
    }

    private static Scenario createScenarioFromData(Map<String, Object> rawData) {
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
        List<SimpleStep> parsedSteps = parseSteps(rawSteps);
        scenario.setSteps(parsedSteps);

        return scenario;
    }

}