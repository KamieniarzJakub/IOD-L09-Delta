package pl.put.poznan.scenario_quality_checker.rest;

import org.springframework.web.bind.annotation.*;
import pl.put.poznan.scenario_quality_checker.logic.NewScenarioParser;

import java.io.IOException;
import java.util.List;

/**
 * Kontroler obsługujący przetwarzanie scenariusza dostarczonego w formacie JSON.
 */
@RestController
public class ScenarioParserController {

    /**
     * Przetwarza scenariusz dostarczony jako JSON i zwraca jego analizowaną reprezentację.
     *
     * @param jsonContent JSON zawierający scenariusz do analizy.
     * @return Lista zawierająca przetworzone kroki scenariusza.
     * @throws RuntimeException jeśli wystąpi błąd podczas przetwarzania JSON.
     */
    @PostMapping("/parse-scenario")
    public List<String> ScenarioParser2(@RequestBody String jsonContent) {
        try {
            return NewScenarioParser.parseScenarioFromString2(jsonContent);
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas przetwarzania pliku JSON", e);
        }
    }
}
