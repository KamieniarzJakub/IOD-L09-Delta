package pl.put.poznan.scenario_quality_checker.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Scenario;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioParser;
import pl.put.poznan.scenario_quality_checker.logic.StepActorValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class StepActorValidatorController {
    @PostMapping("/parse-scenario-steps")
    public List<String> countConditionalSteps(@RequestBody String jsonContent) {
        try {
            Scenario scenario = ScenarioParser.parseScenarioFromString(jsonContent);
            List<String> results = new ArrayList<>();

            List<String> invalidSteps = StepActorValidator.findStepsWithoutActors(scenario);
            if (!invalidSteps.isEmpty()) {
                results.add("Kroki bez aktora:");
                results.addAll(invalidSteps);
            }

            return results;

        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas przetwarzania pliku JSON", e);
        }
    }
}
