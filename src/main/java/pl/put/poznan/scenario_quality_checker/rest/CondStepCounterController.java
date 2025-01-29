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


/**
 * Kontroler obsługujący zliczanie warunkowych i iteracyjnych kroków w scenariuszu.
 */
@RestController
public class CondStepCounterController {
    private final ConditionalStepCounter counterC;
    private final IterativeStepCounter counterI;

    public CondStepCounterController(ConditionalStepCounter counterCond, IterativeStepCounter counterIter) {
        counterC = counterCond;
        counterI = counterIter;
    }

    public CondStepCounterController() {
        counterC = new ConditionalStepCounter();
        counterI = new IterativeStepCounter();
    }

    List<String> countConditionalSteps(Scenario scenario) {
        int conditionalCount = counterC.countConditionalSteps(scenario);
        int iterativeCount = counterI.countIterativeSteps(scenario);
        int totalCount = conditionalCount + iterativeCount;

        List<String> results = new ArrayList<>();

        results.add("Ilość kroków rozpoczynających się od słowa kluczowego: " + totalCount);
        results.add("Ilość decyzji warunkowych: " + conditionalCount);

        return results;
    }

    /**
     * Przetwarza scenariusz dostarczony jako JSON i zwraca liczbę warunkowych oraz iteracyjnych kroków.
     *
     * @param jsonContent JSON zawierający scenariusz do analizy.
     * @return Lista zawierająca informacje o liczbie warunkowych i iteracyjnych kroków.
     * @throws RuntimeException jeśli wystąpi błąd podczas przetwarzania JSON.
     */
    @PostMapping("/count-conditional-steps")
    public List<String> countConditionalSteps(@RequestBody String jsonContent) {
        try {
            Scenario scenario = ScenarioParser.parseScenarioFromString(jsonContent);
            return countConditionalSteps(scenario);
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas przetwarzania pliku JSON", e);
        }
    }
}
