package pl.put.poznan.scenario_quality_checker.rest;

import org.springframework.web.bind.annotation.*;
import pl.put.poznan.scenario_quality_checker.logic.ConditionalStepCounter;
import pl.put.poznan.scenario_quality_checker.logic.IterativeStepCounter;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Scenario;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@RestController
public class CondStepCounterController {

    // POST - w body(raw) trzeba wrzucić jsona -> tego co jest inputem
    @PostMapping("/parse-scenario-cc")
    public List<String> countConditionalSteps(@RequestBody String jsonContent) {
        try {
            Scenario scenario = ScenarioParser.parseScenarioFromString(jsonContent);
            ConditionalStepCounter counterC = new ConditionalStepCounter();
            IterativeStepCounter counterI = new IterativeStepCounter();

            int conditionalCount = counterC.countConditionalSteps(scenario);
            int iterativeCount = counterI.countIterativeSteps(scenario);
            int totalCount = conditionalCount + iterativeCount;

            List<String> results = new ArrayList<>();

            results.add("Ilość kroków rozpoczynających się od słowa kluczowego: " + totalCount);
            results.add("Ilość decyzji warunkowych: " + conditionalCount);

            return results;
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas przetwarzania pliku JSON", e);
        }
    }
}
