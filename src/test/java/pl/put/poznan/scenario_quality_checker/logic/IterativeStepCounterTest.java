package pl.put.poznan.scenario_quality_checker.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.ConditionalStep;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.IterativeStep;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Scenario;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.SimpleStep;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IterativeStepCounterTest {
    IterativeStepCounter iterativeStepCounter;

    @BeforeEach
    void setUp(){
        iterativeStepCounter = new IterativeStepCounter();
    }

    // mock
    @Test
    void testCountIterativeStepsEmpty() {
        Scenario mockScenario = mock(Scenario.class);
        when(mockScenario.getSteps()).thenReturn(List.of());
        assertEquals(0,iterativeStepCounter.countIterativeSteps(mockScenario));
    }

    // mock
    @Test
    void testCountIterativeStepsNull() {
        Scenario mockScenario = mock(Scenario.class);
        when(mockScenario.getSteps()).thenReturn(null);
        assertEquals(0,iterativeStepCounter.countIterativeSteps(mockScenario));
    }

    @Test
    void testCountIterativeSteps() {
        Scenario scenario = new Scenario();
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
        assertEquals(1,iterativeStepCounter.countIterativeSteps(scenario));
    }
}