package pl.put.poznan.scenario_quality_checker.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Scenario;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioParser;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ScenarioController {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioController.class);

    /**
     * Endpoint GET do parsowania scenariusza
     */
    @GetMapping("/parse-scenario-out")
    public List<String> getParsedScenarioFromOut(@RequestParam String filePath) {
        return new ScenarioOut().getParsedScenario(filePath);
    }    public List<String> getParsedScenario(@RequestParam String filePath) {
        logger.info("Przetwarzanie pliku scenariusza: {}", filePath);
        try {
            List<String> outputLogs = new ScenarioOut().getParsedScenario(filePath);
            logger.info("Scenariusz został pomyślnie przetworzony.");
            return outputLogs;
        } catch (Exception e) {
            logger.error("Błąd podczas przetwarzania pliku: {}", filePath, e);
            throw new RuntimeException("Błąd podczas przetwarzania pliku: " + filePath, e);
        }
    }
}
