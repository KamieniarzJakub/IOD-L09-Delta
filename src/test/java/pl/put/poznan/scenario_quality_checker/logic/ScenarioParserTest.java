package pl.put.poznan.scenario_quality_checker.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import java.io.IOException;
import java.util.List;


class ScenarioParserTest {
    Scenario expected1;

    @BeforeEach
    void setUp(){
        expected1 = new Scenario();
        expected1.setTitle("Dodanie książki");
        expected1.setExternalActors(List.of(new Actor(Actor.ActorType.EXTERNAL,"Bibliotekarz"), new Actor(Actor.ActorType.EXTERNAL,"Testowy")));
        expected1.setSystemActors(List.of(new Actor(Actor.ActorType.SYSTEM,"System")));
        expected1.setSteps(List.of(
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
    }

    @Test
    void testParseScenarioFromFile() throws IOException {
        String filename = "InputData/sample_scenario_with_else.json";
        Scenario s = ScenarioParser.parseScenarioFromFile(filename);
        assertEquals(expected1, s);
    }

    @Test
    void testParseScenarioFromString() throws IOException {
        String j = "{\"title\":\"Dodanie książki\",\"actors\":{\"external\":[\"Bibliotekarz\",\"Testowy\"],\"system\":[\"System\"]},\"steps\":[\"Bibliotekarz wybiera opcje dodania nowej pozycji książkowej.\",\"Wyświetla się formularz.\",\"Bibliotekarz podaje dane książki.\",{\"IF\":\"Bibliotekarz pragnie dodać egzemplarze książki\",\"steps\":[\"Bibliotekarz wybiera opcję definiowania egzemplarzy.\",\"System prezentuje zdefiniowane egzemplarze.\",{\"FOR EACH\":\"egzemplarz\",\"steps\":[\"Bibliotekarz wybiera opcję dodania egzemplarza.\",\"System prosi o podanie danych egzemplarza.\",\"Bibliotekarz podaje dane egzemplarza i zatwierdza.\",\"System informuje o poprawnym dodaniu egzemplarza i prezentuje zaktualizowaną listę egzemplarzy.\"]},{\"ELSE\":\"Marek Marek\",\"steps\":[\"elo elo\",\"maro maro\"]}]},\"Bibliotekarz zatwierdza dodanie książki.\",\"System informuje o poprawnym dodaniu książki.\"]}";
        assertEquals(expected1,ScenarioParser.parseScenarioFromString(j));
    }

    @Test
    void testParseNonExistentFile(){
        assertThrows(IOException.class,()->{ScenarioParser.parseScenarioFromFile("THIS FILE DOES NOT EXIST");});
    }

    @Test
    void testParseInvalidJson(){
        assertThrows(ClassCastException.class,()->{ScenarioParser.parseScenarioFromFile("InputData/invalid_json.json");});
        String j = "{\"scenario\":\"Dodanie książki\",\"actors\":{\"external\":\"Bibliotekarz\",\"system\":null},\"steps\":[{\"description\":\"Bibliotekarz wybiera opcje dodania nowej pozycji książkowej.\"},{\"description\":\"System wyświetla się formularz.\"},{\"description\":\"Bibliotekarz podaje dane książki.\"},{\"description\":\"Bibliotekarz zatwierdza dodanie książki.\"},{\"description\":\"System informuje o poprawnym dodaniu książki.\"}]}";
        assertThrows(ClassCastException.class,()->{ScenarioParser.parseScenarioFromString(j);});
    }

}