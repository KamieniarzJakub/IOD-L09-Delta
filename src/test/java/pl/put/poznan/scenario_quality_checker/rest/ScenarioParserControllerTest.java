package pl.put.poznan.scenario_quality_checker.rest;

import org.junit.jupiter.api.Test;
import pl.put.poznan.scenario_quality_checker.logic.ConditionalStepCounter;
import pl.put.poznan.scenario_quality_checker.logic.IterativeStepCounter;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Scenario;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.put.poznan.scenario_quality_checker.rest.TextConstants.*;

class ScenarioParserControllerTest {

    @Test
    void testScenarioParser2ValidJson() {
        String j = "{\"title\":\"Dodanie książki\",\"actors\":{\"external\":[\"Bibliotekarz\",\"Testowy\"],\"system\":[\"System\"]},\"steps\":[\"Bibliotekarz wybiera opcje dodania nowej pozycji książkowej.\",\"Wyświetla się formularz.\",\"Bibliotekarz podaje dane książki.\",{\"IF\":\"Bibliotekarz pragnie dodać egzemplarze książki\",\"steps\":[\"Bibliotekarz wybiera opcję definiowania egzemplarzy.\",\"System prezentuje zdefiniowane egzemplarze.\",{\"FOR EACH\":\"egzemplarz\",\"steps\":[\"Bibliotekarz wybiera opcję dodania egzemplarza.\",\"System prosi o podanie danych egzemplarza.\",\"Bibliotekarz podaje dane egzemplarza i zatwierdza.\",\"System informuje o poprawnym dodaniu egzemplarza i prezentuje zaktualizowaną listę egzemplarzy.\"]},{\"ELSE\":\"Marek Marek\",\"steps\":[\"elo elo\",\"maro maro\"]}]},\"Bibliotekarz zatwierdza dodanie książki.\",\"System informuje o poprawnym dodaniu książki.\"]}";
        CondStepCounterController condStepCounterController = new CondStepCounterController();

        ScenarioParserController scenarioParserController = new ScenarioParserController();
        List<String> result = scenarioParserController.ScenarioParser2(j);
        List<String> expected = List.of("Tytuł: Dodanie książki", "Aktorzy: Bibliotekarz, Testowy", "Aktor systemowy: System", "1. Bibliotekarz wybiera opcje dodania nowej pozycji książkowej.", "2. Wyświetla się formularz.", "3. Bibliotekarz podaje dane książki.", "4. IF: Bibliotekarz pragnie dodać egzemplarze książki", "\t4.1. Bibliotekarz wybiera opcję definiowania egzemplarzy.", "\t4.2. System prezentuje zdefiniowane egzemplarze.", "\t4.3. FOR EACH: egzemplarz", "\t\t4.3.1. Bibliotekarz wybiera opcję dodania egzemplarza.", "\t\t4.3.2. System prosi o podanie danych egzemplarza.", "\t\t4.3.3. Bibliotekarz podaje dane egzemplarza i zatwierdza.", "\t\t4.3.4. System informuje o poprawnym dodaniu egzemplarza i prezentuje zaktualizowaną listę egzemplarzy.", "\t4.4. ELSE: Marek Marek", "\t\t4.4.1. elo elo", "\t\t4.4.2. maro maro", "5. Bibliotekarz zatwierdza dodanie książki.", "6. System informuje o poprawnym dodaniu książki.");
        assertIterableEquals(expected,result);
    }

    @Test
    void testScenarioParser2InvalidJson() {
        String j = "invalid";
        ScenarioParserController scenarioParserController = new ScenarioParserController();

        Exception ex = assertThrows(RuntimeException.class,()->{scenarioParserController.ScenarioParser2(j);});
        assertEquals(runtimeExceptionPhrase, ex.getMessage());
    }
}