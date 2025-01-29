package pl.put.poznan.scenario_quality_checker.logic;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Actor;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Scenario;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Step;

import java.io.IOException;
/**
 * Serializator scenariusza do formatu JSON.
 */
public class ScenarioSerializer extends JsonSerializer<Scenario> {

    /**
     * Konstruktor domyślny.
     */
    public ScenarioSerializer() {
        super();
    }
    /**
     * Serializuje obiekt {@link Scenario} do formatu JSON.
     *
     * @param scenario Scenariusz do serializacji.
     * @param jsonGenerator Generator JSON do zapisu danych.
     * @param serializerProvider Provider serializera.
     * @throws IOException Jeśli wystąpi błąd podczas serializacji.
     */
    @Override
    public void serialize(Scenario scenario, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("title", scenario.getTitle());
        jsonGenerator.writeObjectFieldStart("actors");
        // Serializacja aktorów zewnętrznych
        jsonGenerator.writeArrayFieldStart("external");
        for (Actor actor : scenario.getExternalActors()) {
            jsonGenerator.writeString(actor.getName());
        }
        // Serializacja aktorów systemowych
        jsonGenerator.writeEndArray();
        jsonGenerator.writeArrayFieldStart("system");
        for (Actor actor : scenario.getSystemActors()){
            jsonGenerator.writeString(actor.getName());
        }
        // Serializacja kroków scenariusza
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
        jsonGenerator.writeArrayFieldStart("steps");
        for (Step step : scenario.getSteps()){
            jsonGenerator.writeObject(step);
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }


}
