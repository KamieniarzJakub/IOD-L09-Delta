package pl.put.poznan.scenario_quality_checker.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.scenario_quality_checker.logic.ConditionalStepCounter;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Scenario;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioParser;

import java.io.IOException;

@RestController
public class CondStepCounterController {

    @GetMapping("/parse-scenario-cc")
    public String countConditionalSteps(@RequestParam String filePath) {
        try {
            Scenario scenario = ScenarioParser.parseScenarioFromFile(filePath);
            ConditionalStepCounter counter = new ConditionalStepCounter();
            return "Ilość decyzji warunkowych: " + counter.countConditionalSteps(scenario);
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas przetwarzania pliku: " + filePath, e);
        }
    }
}
