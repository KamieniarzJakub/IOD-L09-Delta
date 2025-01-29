package pl.put.poznan.scenario_quality_checker.rest;
                                                                                                                                                            
import org.springframework.web.bind.annotation.GetMapping;                                                                                                  
import org.springframework.web.bind.annotation.RequestParam;                                                                                                
import org.springframework.web.bind.annotation.RestController;                                                                                              
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Scenario;                                                                               
import pl.put.poznan.scenario_quality_checker.logic.ScenarioParser;                                                                                         
import java.io.IOException;

/**
 * Kontroler obsługujący przetwarzanie statycznego scenariusza z pliku.
 */
@RestController                                                                                                                                         
public class StaticScenarioController {
    /**
     * Przetwarza scenariusz pobrany z pliku i zwraca jego analizowaną reprezentację.
     *
     * @param filePath Ścieżka do pliku zawierającego scenariusz.
     * @return Obiekt {@link Scenario} reprezentujący przetworzony scenariusz.
     * @throws RuntimeException jeśli wystąpi błąd podczas odczytu pliku.
     */
    @GetMapping("/parse-static-scenario")
    public Scenario getParsedScenario(@RequestParam String filePath) {                                                                                      
        try {                                                                                                                                               
            return ScenarioParser.parseScenarioFromFile(filePath);                                                                                          
        } catch (IOException e) {                                                                                                                           
            throw new RuntimeException("Błąd podczas przetwarzania pliku: " + filePath, e);                                                                 
        }                                                                                                                                                   
    }                                                                                                                                                       

}