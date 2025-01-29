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

/**
 * Kontroler obsługujący walidację kroków scenariusza pod kątem obecności aktorów.
 */
@RestController
public class StepActorValidatorController {

    StepActorValidator stepActorValidator;

    public StepActorValidatorController(){
        stepActorValidator = null;
    }

    public StepActorValidatorController(Scenario scenario){
        stepActorValidator = new StepActorValidator(scenario);
    }

    public StepActorValidatorController(StepActorValidator validator){
        stepActorValidator = validator;
    }

    public List<String> getStepsWithoutActors(){
        List<String> results = new ArrayList<>();
        List<String> invalidSteps = stepActorValidator.findStepsWithoutActors();
        if (!invalidSteps.isEmpty()) {
            results.add("Kroki bez aktora:");
            results.addAll(invalidSteps);
        }
        return results;
    }

    /**
     * Analizuje scenariusz i zwraca listę kroków, które nie mają przypisanych aktorów.
     *
     * @param jsonContent JSON zawierający scenariusz do analizy.
     * @return Lista zawierająca kroki scenariusza, które nie mają aktorów.
     * @throws RuntimeException jeśli wystąpi błąd podczas przetwarzania JSON.
     */
    @PostMapping("/steps-without-actors")
    public List<String> getStepsWithoutActors(@RequestBody String jsonContent) {
        try {
            Scenario scenario = ScenarioParser.parseScenarioFromString(jsonContent);
            stepActorValidator = new StepActorValidator(scenario);
            return getStepsWithoutActors();
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas przetwarzania pliku JSON", e);
        }
    }
}
