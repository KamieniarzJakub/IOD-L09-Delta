package pl.put.poznan.scenario_quality_checker.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Scenario;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioParser;

import java.io.IOException;

@RestController
public class ScenarioController {

    @GetMapping("/parse-scenario")
    public Scenario getParsedScenario(@RequestParam String filePath) {
        try {
            return ScenarioParser.parseScenarioFromFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas przetwarzania pliku: " + filePath, e);
        }
    }
}
