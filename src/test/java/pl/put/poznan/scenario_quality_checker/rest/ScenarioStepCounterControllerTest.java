package pl.put.poznan.scenario_quality_checker.rest;

import org.junit.jupiter.api.Test;
import pl.put.poznan.scenario_quality_checker.logic.ConditionalStepCounter;
import pl.put.poznan.scenario_quality_checker.logic.IterativeStepCounter;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.*;
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

    @Test
    void testCountSubStepsRealScenario() {
        ScenarioStepCounterController stepCounterController = new ScenarioStepCounterController();

        Scenario scenario = new Scenario();
        scenario.setTitle("Dodanie książki");
        scenario.setExternalActors(List.of(new Actor(Actor.ActorType.EXTERNAL,"Bibliotekarz"), new Actor(Actor.ActorType.EXTERNAL,"Testowy")));
        scenario.setSystemActors(List.of(new Actor(Actor.ActorType.SYSTEM,"System")));
        scenario.setSteps(List.of(
                new SimpleStep("Bibliotekarz wybiera opcje dodania nowej pozycji książkowej."),
                new SimpleStep("Wyświetla się formularz."),
                new SimpleStep("Bibliotekarz podaje dane książki."),
                new ConditionalStep(ConditionalStep.ConditionalType.IF, "Bibliotekarz pragnie dodać egzemplarze książki",List.of(
                        new SimpleStep("Bibliotekarz wybiera opcję definiowania egzemplarzy."),
                        new SimpleStep("System prezentuje zdefiniowane egzemplarze."),
                        new IterativeStep("egzemplarz",List.of(
                                new SimpleStep("Bibliotekarz wybiera opcję dodania egzemplarza."),
                                new SimpleStep("System prosi o podanie danych egzemplarza."),
                                new SimpleStep("Bibliotekarz podaje dane egzemplarza i zatwierdza."),
                                new SimpleStep("System informuje o poprawnym dodaniu egzemplarza i prezentuje zaktualizowaną listę egzemplarzy.")
                        )),
                        new ConditionalStep(ConditionalStep.ConditionalType.ELSE, "Marek Marek", List.of(
                                new SimpleStep("elo elo"),
                                new SimpleStep("maro maro")
                        ))
                )),
                new SimpleStep("Bibliotekarz zatwierdza dodanie książki."),
                new SimpleStep("System informuje o poprawnym dodaniu książki.")
        ));

        List<String> result = stepCounterController.countSubSteps(scenario);
        List<String> expected = List.of(mainStepsPhrase+6, allStepsPhrase+16);
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

    // mock
    @Test
    void testCountSubStepsInvalidJson() {
        String j = "invalid";
        ScenarioStepCounter mockScenarioStepCounterAllSteps = mock(ScenarioStepCounter.class);
        ScenarioStepCounter mockScenarioStepCounterMainSteps = mock(ScenarioStepCounter.class);
        when(mockScenarioStepCounterAllSteps.getStepCount()).thenReturn(0);
        when(mockScenarioStepCounterMainSteps.getStepCount()).thenReturn(0);

        ScenarioStepCounterController stepCounterController = new ScenarioStepCounterController(
                mockScenarioStepCounterMainSteps, mockScenarioStepCounterAllSteps);

        Exception ex = assertThrows(RuntimeException.class,()->{stepCounterController.countSubSteps(j);});
        assertEquals(runtimeExceptionPhrase, ex.getMessage());
    }

    @Test
    void testCountSubStepsRealJson() {

        String j = "{\"title\":\"Dodanie książki\",\"actors\":{\"external\":[\"Bibliotekarz\",\"Testowy\"],\"system\":[\"System\"]},\"steps\":[\"Bibliotekarz wybiera opcje dodania nowej pozycji książkowej.\",\"Wyświetla się formularz.\",\"Bibliotekarz podaje dane książki.\",{\"IF\":\"Bibliotekarz pragnie dodać egzemplarze książki\",\"steps\":[\"Bibliotekarz wybiera opcję definiowania egzemplarzy.\",\"System prezentuje zdefiniowane egzemplarze.\",{\"FOR EACH\":\"egzemplarz\",\"steps\":[\"Bibliotekarz wybiera opcję dodania egzemplarza.\",\"System prosi o podanie danych egzemplarza.\",\"Bibliotekarz podaje dane egzemplarza i zatwierdza.\",\"System informuje o poprawnym dodaniu egzemplarza i prezentuje zaktualizowaną listę egzemplarzy.\"]},{\"ELSE\":\"Marek Marek\",\"steps\":[\"elo elo\",\"maro maro\"]}]},\"Bibliotekarz zatwierdza dodanie książki.\",\"System informuje o poprawnym dodaniu książki.\"]}";
        CondStepCounterController condStepCounterController = new CondStepCounterController();

        ScenarioStepCounterController stepCounterController = new ScenarioStepCounterController();
        List<String> result = stepCounterController.countSubSteps(j);
        List<String> expected = List.of(mainStepsPhrase+6, allStepsPhrase+16);
        assertEquals(expected,result);
    }
}