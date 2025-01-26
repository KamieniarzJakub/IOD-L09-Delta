package pl.put.poznan.scenario_quality_checker.logic;

import org.junit.jupiter.api.Test;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StepActorValidatorTest {

    // mock
    @Test
    void testFindStepsWithoutActorsEmptyScenario() {
        Scenario mockScenario = mock(Scenario.class);
        when(mockScenario.getSteps()).thenReturn(List.of());
        when(mockScenario.getSystemActors()).thenReturn(List.of());
        when(mockScenario.getExternalActors()).thenReturn(List.of());
        StepActorValidator stepActorValidator = new StepActorValidator(mockScenario);

        assertIterableEquals(List.of(),stepActorValidator.findStepsWithoutActors());
    }

    // mock
    @Test
    void testFindStepsWithoutActors() {
        Scenario mockScenario = mock(Scenario.class);
        when(mockScenario.getSteps()).thenReturn(List.of(new SimpleStep("Bibliotekarz wybiera opcje dodania nowej pozycji książkowej."),
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
        when(mockScenario.getSystemActors()).thenReturn(List.of(new Actor(Actor.ActorType.SYSTEM,"System")));
        when(mockScenario.getExternalActors()).thenReturn(List.of(
                new Actor(Actor.ActorType.EXTERNAL,"Bibliotekarz"),
                new Actor(Actor.ActorType.EXTERNAL,"Testowy"))
        );
        StepActorValidator stepActorValidator = new StepActorValidator(mockScenario);


        assertIterableEquals(List.of("2. Wyświetla się formularz.", "4.4.1. elo elo", "4.4.2. maro maro"),
                stepActorValidator.findStepsWithoutActors());
    }

}
