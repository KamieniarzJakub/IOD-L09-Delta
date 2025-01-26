package pl.put.poznan.scenario_quality_checker.rest;

import org.junit.jupiter.api.Test;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pl.put.poznan.scenario_quality_checker.rest.TextConstants.runtimeExceptionPhrase;

class StaticScenarioControllerTest {

    @Test
    void testGetParsedScenarioInvalidPath() {
        String p = "invalid";
        String ex_str = "Błąd podczas przetwarzania pliku: ";
        StaticScenarioController staticScenarioController = new StaticScenarioController();

        Exception ex = assertThrows(RuntimeException.class,()->{staticScenarioController.getParsedScenario(p);});
        assertEquals(ex_str+p, ex.getMessage());
    }

    @Test
    void testGetParsedScenarioValidPath() {
        String p = "InputData/sample_scenario_with_else.json";
        StaticScenarioController staticScenarioController = new StaticScenarioController();
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

        assertEquals(scenario,staticScenarioController.getParsedScenario(p));
    }
}