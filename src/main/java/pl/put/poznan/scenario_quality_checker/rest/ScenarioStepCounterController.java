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

    final ScenarioStepCounter mainStepCounter;
    final ScenarioStepCounter allStepCounter;

    public ScenarioStepCounterController(ScenarioStepCounter stepCounter1, ScenarioStepCounter stepCounter2){
        mainStepCounter = stepCounter1;
        allStepCounter = stepCounter2;
    }

    public ScenarioStepCounterController(){
        mainStepCounter = new ScenarioStepCounter(false);
        allStepCounter = new ScenarioStepCounter(true);
    }

    List<String> countSubSteps(Scenario scenario) {
        List<String> results = new ArrayList<>();

        for (Step step : scenario.getSteps()) {
            step.accept(mainStepCounter);
        }
        results.add("Liczba głównych kroków: " + mainStepCounter.getStepCount());

        for (Step step : scenario.getSteps()) {
            step.accept(allStepCounter);
        }
        results.add("Liczba wszystkich kroków (w tym podkroków): " + allStepCounter.getStepCount());
        return results;
    }

    @PostMapping("/count-steps")
    public List<String> countSubSteps(@RequestBody String jsonContent) {
        try {
            Scenario scenario = ScenarioParser.parseScenarioFromString(jsonContent);
            return countSubSteps(scenario);
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas przetwarzania pliku JSON", e);
        }
    }

}
