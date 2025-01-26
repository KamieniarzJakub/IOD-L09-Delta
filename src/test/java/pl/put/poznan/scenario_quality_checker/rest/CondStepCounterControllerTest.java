package pl.put.poznan.scenario_quality_checker.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.scenario_quality_checker.logic.ConditionalStepCounter;
import pl.put.poznan.scenario_quality_checker.logic.IterativeStepCounter;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Scenario;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void testCountConditionalStepsJsonString() {
    }

    @Test
    void testCountConditionalStepsInvalidJsonString() {
    }
}