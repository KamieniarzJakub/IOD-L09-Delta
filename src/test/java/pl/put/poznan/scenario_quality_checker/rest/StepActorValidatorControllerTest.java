package pl.put.poznan.scenario_quality_checker.rest;

import org.junit.jupiter.api.Test;
import pl.put.poznan.scenario_quality_checker.logic.StepActorValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.put.poznan.scenario_quality_checker.rest.TextConstants.runtimeExceptionPhrase;
import static pl.put.poznan.scenario_quality_checker.rest.TextConstants.stepsWithoutActorsPhrase;


class StepActorValidatorControllerTest {

    // mock
    @Test
    void testGetStepsWithoutActorsScenarioEmptyList() {
        StepActorValidator mockStepActorValidator = mock(StepActorValidator.class);
        when(mockStepActorValidator.findStepsWithoutActors()).thenReturn(List.of());
        StepActorValidatorController stepActorValidatorController =
                new StepActorValidatorController(mockStepActorValidator);

        assertIterableEquals(List.of(),stepActorValidatorController.getStepsWithoutActors());
    }

    // mock
    @Test
    void testGetStepsWithoutActorsScenario() {
        StepActorValidator mockStepActorValidator = mock(StepActorValidator.class);
        List<String> steps = List.of("2. Wyświetla się formularz.", "4.4.1. elo elo", "4.4.2. maro maro");
        when(mockStepActorValidator.findStepsWithoutActors()).thenReturn(
                steps);

        StepActorValidatorController stepActorValidatorController =
                new StepActorValidatorController(mockStepActorValidator);

        List<String> expected = new java.util.ArrayList<>(steps);
        expected.add(0,stepsWithoutActorsPhrase);
        assertIterableEquals(expected,stepActorValidatorController.getStepsWithoutActors());
    }

    @Test
    void testGetStepsWithoutActorsJsonString() {
        List<String> steps = List.of("2. Wyświetla się formularz.", "4.4.1. elo elo", "4.4.2. maro maro");

        StepActorValidatorController stepActorValidatorController =
                new StepActorValidatorController();

        String j = "{\"title\":\"Dodanie książki\",\"actors\":{\"external\":[\"Bibliotekarz\",\"Testowy\"],\"system\":[\"System\"]},\"steps\":[\"Bibliotekarz wybiera opcje dodania nowej pozycji książkowej.\",\"Wyświetla się formularz.\",\"Bibliotekarz podaje dane książki.\",{\"IF\":\"Bibliotekarz pragnie dodać egzemplarze książki\",\"steps\":[\"Bibliotekarz wybiera opcję definiowania egzemplarzy.\",\"System prezentuje zdefiniowane egzemplarze.\",{\"FOR EACH\":\"egzemplarz\",\"steps\":[\"Bibliotekarz wybiera opcję dodania egzemplarza.\",\"System prosi o podanie danych egzemplarza.\",\"Bibliotekarz podaje dane egzemplarza i zatwierdza.\",\"System informuje o poprawnym dodaniu egzemplarza i prezentuje zaktualizowaną listę egzemplarzy.\"]},{\"ELSE\":\"Marek Marek\",\"steps\":[\"elo elo\",\"maro maro\"]}]},\"Bibliotekarz zatwierdza dodanie książki.\",\"System informuje o poprawnym dodaniu książki.\"]}";
        List<String> expected = new java.util.ArrayList<>(steps);
        expected.add(0,stepsWithoutActorsPhrase);
        assertIterableEquals(expected,stepActorValidatorController.getStepsWithoutActors(j));
    }

    @Test
    void testGetStepsWithoutActorsInvalidJsonString() {
        StepActorValidator mockStepActorValidator = mock(StepActorValidator.class);
        List<String> steps = List.of("2. Wyświetla się formularz.", "4.4.1. elo elo", "4.4.2. maro maro");
        when(mockStepActorValidator.findStepsWithoutActors()).thenReturn(
                steps);

        StepActorValidatorController stepActorValidatorController =
                new StepActorValidatorController(mockStepActorValidator);

        String j = "invalid";

        Exception ex = assertThrows(RuntimeException.class,()->{stepActorValidatorController.getStepsWithoutActors(j);});
        assertEquals(runtimeExceptionPhrase, ex.getMessage());
    }
}