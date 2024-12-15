package pl.put.poznan.scenario_quality_checker.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Scenario;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Step;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioParser;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioStepCounter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ScenarioStepCounterController {

    @PostMapping("/count-steps")
    public List<String> countSubSteps(@RequestBody String jsonContent) {
        try {
            Scenario scenario = ScenarioParser.parseScenarioFromString(jsonContent);
            List<String> results = new ArrayList<>();

            ScenarioStepCounter mainStepCounter = new ScenarioStepCounter(false);
            for (Step step : scenario.getSteps()) {
                step.accept(mainStepCounter);
            }
            results.add("Liczba głównych kroków: " + mainStepCounter.getStepCount());

            ScenarioStepCounter allStepCounter = new ScenarioStepCounter(true);
            for (Step step : scenario.getSteps()) {
                step.accept(allStepCounter);
            }
            results.add("Liczba wszystkich kroków (w tym podkroków): " + allStepCounter.getStepCount());

            return results;
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas przetwarzania pliku JSON", e);
        }
    }

}
