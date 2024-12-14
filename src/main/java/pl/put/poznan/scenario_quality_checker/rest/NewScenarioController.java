package pl.put.poznan.scenario_quality_checker.rest;

import org.springframework.web.bind.annotation.*;
import pl.put.poznan.scenario_quality_checker.logic.NewScenarioParser;

import java.io.IOException;
import java.util.List;

@RestController
public class NewScenarioController {

    // POST - w body(raw) trzeba wrzucić jsona -> tego co jest inputem
    @PostMapping("/parse-scenario-2")
    public List<String> ScenarioParser2(@RequestBody String jsonContent) {
        try {
            return NewScenarioParser.parseScenarioFromString2(jsonContent);
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas przetwarzania pliku JSON", e);
        }
    }
}
