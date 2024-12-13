package pl.put.poznan.scenario_quality_checker.logic;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Actor;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Scenario;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.SimpleStep;

import java.io.IOException;

public class ScenarioSerializer extends JsonSerializer<Scenario> {
    public ScenarioSerializer() {
        super();
    }

    @Override
    public void serialize(Scenario scenario, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("title", scenario.getTitle());
        jsonGenerator.writeObjectFieldStart("actors");
        jsonGenerator.writeArrayFieldStart("external");
        for (Actor actor : scenario.getExternalActors()) {
            jsonGenerator.writeString(actor.getName());
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeArrayFieldStart("internal");
        for (Actor actor : scenario.getSystemActors()){
            jsonGenerator.writeString(actor.getName());
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
        jsonGenerator.writeArrayFieldStart("steps");
        for (SimpleStep step : scenario.getSteps()){
            jsonGenerator.writeObject(step);
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }


}
