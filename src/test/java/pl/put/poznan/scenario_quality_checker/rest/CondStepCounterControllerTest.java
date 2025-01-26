package pl.put.poznan.scenario_quality_checker.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.scenario_quality_checker.logic.ConditionalStepCounter;
import pl.put.poznan.scenario_quality_checker.logic.IterativeStepCounter;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Scenario;

import java.net.ConnectException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CondStepCounterControllerTest {

    final String keywordPhrase = "Ilość kroków rozpoczynających się od słowa kluczowego: ";
    final String conditionalPhrase = "Ilość decyzji warunkowych: ";
    final String runtimeExceptionPhrase = "Błąd podczas przetwarzania pliku JSON";

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCountConditionalStepsScenario() {
        ConditionalStepCounter mockConditionalStepCounter = mock(ConditionalStepCounter.class);
        when(mockConditionalStepCounter.countConditionalSteps(any(Scenario.class))).thenReturn(2);
        IterativeStepCounter mockIterativeStepCounter = mock(IterativeStepCounter.class);
        when(mockIterativeStepCounter.countIterativeSteps(any(Scenario.class))).thenReturn(5);
        CondStepCounterController condStepCounterController = new CondStepCounterController(
                mockConditionalStepCounter, mockIterativeStepCounter);

        List<String> result = condStepCounterController.countConditionalSteps(new Scenario());
        List<String> expected = List.of(keywordPhrase+(5+2), conditionalPhrase+2);
        assertEquals(expected,result);
    }

    @Test
    void testCountConditionalStepsEmptyScenario() {
        ConditionalStepCounter mockConditionalStepCounter = mock(ConditionalStepCounter.class);
        when(mockConditionalStepCounter.countConditionalSteps(any(Scenario.class))).thenReturn(0);
        IterativeStepCounter mockIterativeStepCounter = mock(IterativeStepCounter.class);
        when(mockIterativeStepCounter.countIterativeSteps(any(Scenario.class))).thenReturn(0);
        CondStepCounterController condStepCounterController = new CondStepCounterController(
                mockConditionalStepCounter, mockIterativeStepCounter);

        List<String> result = condStepCounterController.countConditionalSteps(new Scenario());
        List<String> expected = List.of(keywordPhrase+0, conditionalPhrase+0);
        assertEquals(expected,result);
    }

    @Test
    void testCountConditionalStepsMockJsonString() {
        String j = "{\"title\":\"\",\"actors\":{\"external\":[],\"system\":[\"System\"]},\"steps\":[]}";
        ConditionalStepCounter mockConditionalStepCounter = mock(ConditionalStepCounter.class);
        when(mockConditionalStepCounter.countConditionalSteps(any(Scenario.class))).thenReturn(7);
        IterativeStepCounter mockIterativeStepCounter = mock(IterativeStepCounter.class);
        when(mockIterativeStepCounter.countIterativeSteps(any(Scenario.class))).thenReturn(8);
        CondStepCounterController condStepCounterController = new CondStepCounterController(
                mockConditionalStepCounter, mockIterativeStepCounter);

        List<String> result = condStepCounterController.countConditionalSteps(j);
        List<String> expected = List.of(keywordPhrase+(7+8), conditionalPhrase+7);
        assertEquals(expected,result);
    }

    @Test
    void testCountConditionalStepsRealJsonString() {
        String j = "{\"title\":\"Dodanie książki\",\"actors\":{\"external\":[\"Bibliotekarz\",\"Testowy\"],\"system\":[\"System\"]},\"steps\":[\"Bibliotekarz wybiera opcje dodania nowej pozycji książkowej.\",\"Wyświetla się formularz.\",\"Bibliotekarz podaje dane książki.\",{\"IF\":\"Bibliotekarz pragnie dodać egzemplarze książki\",\"steps\":[\"Bibliotekarz wybiera opcję definiowania egzemplarzy.\",\"System prezentuje zdefiniowane egzemplarze.\",{\"FOR EACH\":\"egzemplarz\",\"steps\":[\"Bibliotekarz wybiera opcję dodania egzemplarza.\",\"System prosi o podanie danych egzemplarza.\",\"Bibliotekarz podaje dane egzemplarza i zatwierdza.\",\"System informuje o poprawnym dodaniu egzemplarza i prezentuje zaktualizowaną listę egzemplarzy.\"]},{\"ELSE\":\"Marek Marek\",\"steps\":[\"elo elo\",\"maro maro\"]}]},\"Bibliotekarz zatwierdza dodanie książki.\",\"System informuje o poprawnym dodaniu książki.\"]}";
        CondStepCounterController condStepCounterController = new CondStepCounterController();

        List<String> result = condStepCounterController.countConditionalSteps(j);
        List<String> expected = List.of(keywordPhrase+(3), conditionalPhrase+2);
        assertEquals(expected,result);
    }

    @Test
    void testCountConditionalStepsInvalidJsonString() {
        String j = "invalid";
        ConditionalStepCounter mockConditionalStepCounter = mock(ConditionalStepCounter.class);
        when(mockConditionalStepCounter.countConditionalSteps(any(Scenario.class))).thenReturn(0);
        IterativeStepCounter mockIterativeStepCounter = mock(IterativeStepCounter.class);
        when(mockIterativeStepCounter.countIterativeSteps(any(Scenario.class))).thenReturn(0);
        CondStepCounterController condStepCounterController = new CondStepCounterController(
                mockConditionalStepCounter, mockIterativeStepCounter);

        Exception ex = assertThrows(RuntimeException.class,()->{condStepCounterController.countConditionalSteps(j);});
        assertEquals(runtimeExceptionPhrase, ex.getMessage());
    }
}