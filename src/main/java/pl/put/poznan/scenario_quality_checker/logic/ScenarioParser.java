package pl.put.poznan.scenario_quality_checker.logic;

import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScenarioParser {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioParser.class);

    public static Scenario parseScenarioFromFile(String path) throws IOException {
        logger.debug("Rozpoczynam parsowanie scenariusza z pliku: {}", path);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> rawData = mapper.readValue(new File(path), Map.class);

        Scenario scenario = createScenarioFromData(rawData);

        logger.info("Załadowano scenariusz z tytułem: {}", scenario.getTitle());
        logger.info("Aktorzy zewnętrzni: {}", String.join(", ", parseActors(scenario.getExternalActors())));
        logger.info("Aktorzy systemowi: {}", String.join(", ", parseActors(scenario.getSystemActors())));

        // Printowanie kroków kolejno
        logger.debug("Rozpoczynam wypisywanie kroków scenariusza.");
        printSteps(scenario.getSteps(), "");

        // Użycie wizytatora - liczenie głównych kroków
        ScenarioStepCounter mainStepCounter = new ScenarioStepCounter(false);
        for (Step step : scenario.getSteps()) {
            step.accept(mainStepCounter);
        }
        logger.info("Liczba głównych kroków: {}", mainStepCounter.getStepCount());

        // Użycie wizytatora - liczenie wszystkich kroków, w tym podkroków
        ScenarioStepCounter allStepCounter = new ScenarioStepCounter(true);
        for (Step step : scenario.getSteps()) {
            step.accept(allStepCounter);
        }
        logger.info("Liczba wszystkich kroków (w tym podkroków): {}", allStepCounter.getStepCount());

        // Liczenie warunkowych kroków
        ConditionalStepCounter conditionCounter = new ConditionalStepCounter();
        logger.info("Liczba wszystkich kroków warunkowych: {}", conditionCounter.countConditionalSteps(scenario));

        // Walidacja kroków bez aktora
        List<String> invalidSteps = StepActorValidator.findStepsWithoutActors(scenario);
        if (invalidSteps.isEmpty()) {
            logger.info("Wszystkie kroki zaczynają się od aktora.");
        } else {
            logger.warn("Kroki bez aktora:");
            invalidSteps.forEach(step -> logger.warn(step));
        }

        return scenario;
    }

    private static List<String> parseActors(List<Actor> actors) {
        logger.debug("Parsowanie listy aktorów: {}", actors);
        return actors.stream().map(Actor::getName).collect(Collectors.toList());
    }

    private static List<Step> parseSteps(List<?> steps) {
        logger.debug("Parsowanie listy kroków: {}", steps);
        return steps.stream()
                .map(ScenarioParser::parseStep)
                .collect(Collectors.toList());
    }

    private static Step parseStep(Object step) {
        logger.debug("Parsowanie kroku: {}", step);
        if (step instanceof SimpleStep) {
            return (SimpleStep) step;
        } else if (step instanceof String) {
            return new SimpleStep((String) step);
        } else if (step instanceof Map) {
            Map<?, ?> stepMap = (Map<?, ?>) step;

            if (stepMap.containsKey("IF")) {
                String condition = (String) stepMap.get("IF");
                List<Step> innerSteps = parseSteps((List<?>) stepMap.get("steps"));
                logger.debug("Stworzono krok warunkowy typu IF z warunkiem: {}", condition);
                return new ConditionalStep(ConditionalStep.ConditionalType.IF, condition, innerSteps);
            } else if (stepMap.containsKey("FOR EACH")) {
                String loopVariable = (String) stepMap.get("FOR EACH");
                List<Step> innerSteps = parseSteps((List<?>) stepMap.get("steps"));
                logger.debug("Stworzono krok iteracyjny FOR EACH z zmienną: {}", loopVariable);
                return new IterativeStep(loopVariable, innerSteps);
            } else if (stepMap.containsKey("ELSE")) {
                String loopVariable = (String) stepMap.get("ELSE");
                List<Step> innerSteps = parseSteps((List<?>) stepMap.get("steps"));
                logger.debug("Stworzono krok warunkowy ELSE z warunkiem: {}", loopVariable);
                return new ConditionalStep(ConditionalStep.ConditionalType.ELSE, loopVariable, innerSteps);
            }
        }

        throw new IllegalArgumentException("Nieoczekiwany format kroku: " + step);
    }

    private static void printSteps(List<Step> steps, String prefix) {
        if (steps == null) return;

        int stepCounter = 1;

        for (Step step : steps) {
            String currentPrefix = prefix.isEmpty() ? String.valueOf(stepCounter) : prefix + "." + stepCounter;

            if (step instanceof ConditionalStep) {
                ConditionalStep condStep = (ConditionalStep) step;
                logger.info("{} {}: {}", currentPrefix, condStep.getConditionalType(), condStep.getCondition());
                printSteps(condStep.getSteps(), "\t" + currentPrefix);
            } else if (step instanceof IterativeStep) {
                IterativeStep iterStep = (IterativeStep) step;
                logger.info("{} FOR EACH: {}", currentPrefix, iterStep.getLoopVariable());
                printSteps(iterStep.getSteps(), "\t" + currentPrefix);
            } else if (step instanceof SimpleStep) {
                logger.info("{} {}", currentPrefix, ((SimpleStep) step).getDescription());
            }
            stepCounter++;
        }
    }

    public static Scenario parseScenarioFromString(String jsonContent) throws IOException {
        logger.debug("Rozpoczynam parsowanie scenariusza z zawartości JSON.");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> rawData = mapper.readValue(jsonContent, Map.class);

        Scenario scenario = createScenarioFromData(rawData);
        logger.info("Załadowano scenariusz z tytułem: {}", scenario.getTitle());

        return scenario;
    }

    private static Scenario createScenarioFromData(Map<String, Object> rawData) {
        logger.debug("Tworzenie scenariusza z danych wejściowych: {}", rawData);

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

        logger.debug("Scenariusz został pomyślnie utworzony.");

        return scenario;
    }
}
