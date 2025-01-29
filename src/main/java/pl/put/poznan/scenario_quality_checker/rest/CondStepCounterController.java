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
            // Parsowanie scenariusza z formatu JSON
            Scenario scenario = ScenarioParser.parseScenarioFromString(jsonContent);
            // Inicjalizacja liczników dla kroków warunkowych i iteracyjnych
            ConditionalStepCounter counterC = new ConditionalStepCounter();
            IterativeStepCounter counterI = new IterativeStepCounter();
            // Obliczanie liczby kroków warunkowych i iteracyjnych
            int conditionalCount = counterC.countConditionalSteps(scenario);
            int iterativeCount = counterI.countIterativeSteps(scenario);
            int totalCount = conditionalCount + iterativeCount;
            // Tworzenie listy wyników
            List<String> results = new ArrayList<>();

            results.add("Ilość kroków rozpoczynających się od słowa kluczowego: " + totalCount);
            results.add("Ilość decyzji warunkowych: " + conditionalCount);

            return results;
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas przetwarzania pliku JSON", e);
        }
    }
}
