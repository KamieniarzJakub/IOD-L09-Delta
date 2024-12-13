package pl.put.poznan.scenario_quality_checker.logic;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.SimpleStep;

import java.io.IOException;

public class SimpleStepSerializer extends JsonSerializer<SimpleStep> {
    @Override
    public void serialize(SimpleStep simpleStep, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(simpleStep.getDescription());
    }
}
