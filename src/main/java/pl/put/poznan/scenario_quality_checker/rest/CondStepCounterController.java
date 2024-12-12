package pl.put.poznan.scenario_quality_checker.rest;

import org.springframework.web.bind.annotation.*;
import pl.put.poznan.scenario_quality_checker.logic.ConditionalStepCounter;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Scenario;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioParser;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CondStepCounterController {

    // POST - w body(raw) trzeba wrzucić jsona -> tego co jest inputem
    @PostMapping("/parse-scenario-cc")
    public String countConditionalSteps(@RequestBody String jsonContent) {
        try {
            Scenario scenario = ScenarioParser.parseScenarioFromString(jsonContent);
            ConditionalStepCounter counter = new ConditionalStepCounter();
            return "Ilość decyzji warunkowych: " + counter.countConditionalSteps(scenario);
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas przetwarzania pliku JSON", e);
        }
    }
}
