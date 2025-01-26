package pl.put.poznan.scenario_quality_checker.rest;

import org.junit.jupiter.api.Test;
import pl.put.poznan.scenario_quality_checker.logic.ConditionalStepCounter;
import pl.put.poznan.scenario_quality_checker.logic.IterativeStepCounter;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Scenario;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioStepCounter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.put.poznan.scenario_quality_checker.rest.TextConstants.*;

class ScenarioStepCounterControllerTest {

    // mock
    @Test
    void testCountSubStepsScenario() {
        ScenarioStepCounter mockScenarioStepCounterAllSteps = mock(ScenarioStepCounter.class);
        ScenarioStepCounter mockScenarioStepCounterMainSteps = mock(ScenarioStepCounter.class);
        when(mockScenarioStepCounterAllSteps.getStepCount()).thenReturn(8);
        when(mockScenarioStepCounterMainSteps.getStepCount()).thenReturn(2);

        ScenarioStepCounterController stepCounterController = new ScenarioStepCounterController(
                mockScenarioStepCounterMainSteps, mockScenarioStepCounterAllSteps);

        Scenario scenario = mock(Scenario.class);
        when(scenario.getSteps()).thenReturn(List.of());

        List<String> result = stepCounterController.countSubSteps(scenario);
        List<String> expected = List.of(mainStepsPhrase+2, allStepsPhrase+8);
        assertEquals(expected,result);
    }

    // mock
    @Test
    void testCountSubStepsEmptyScenario() {
        ScenarioStepCounter mockScenarioStepCounterAllSteps = mock(ScenarioStepCounter.class);
        ScenarioStepCounter mockScenarioStepCounterMainSteps = mock(ScenarioStepCounter.class);
        when(mockScenarioStepCounterAllSteps.getStepCount()).thenReturn(0);
        when(mockScenarioStepCounterMainSteps.getStepCount()).thenReturn(0);

        ScenarioStepCounterController stepCounterController = new ScenarioStepCounterController(
                mockScenarioStepCounterMainSteps, mockScenarioStepCounterAllSteps);

        Scenario scenario = mock(Scenario.class);
        when(scenario.getSteps()).thenReturn(List.of());

        List<String> result = stepCounterController.countSubSteps(scenario);
        List<String> expected = List.of(mainStepsPhrase+0, allStepsPhrase+0);
        assertEquals(expected,result);
    }

    // mock
    @Test
    void testCountSubStepsJsonString() {
        String j = "{\"title\":\"\",\"actors\":{\"external\":[],\"system\":[\"System\"]},\"steps\":[]}";
        ScenarioStepCounter mockScenarioStepCounterAllSteps = mock(ScenarioStepCounter.class);
        ScenarioStepCounter mockScenarioStepCounterMainSteps = mock(ScenarioStepCounter.class);
        when(mockScenarioStepCounterAllSteps.getStepCount()).thenReturn(8);
        when(mockScenarioStepCounterMainSteps.getStepCount()).thenReturn(2);

        ScenarioStepCounterController stepCounterController = new ScenarioStepCounterController(
                mockScenarioStepCounterMainSteps, mockScenarioStepCounterAllSteps);

        List<String> result = stepCounterController.countSubSteps(j);
        List<String> expected = List.of(mainStepsPhrase+2, allStepsPhrase+8);
        assertEquals(expected,result);
    }
}