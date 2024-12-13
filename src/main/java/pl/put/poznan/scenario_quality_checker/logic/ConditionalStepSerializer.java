package pl.put.poznan.scenario_quality_checker.logic;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.ConditionalStep;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Step;

import java.io.IOException;

public class ConditionalStepSerializer extends JsonSerializer<ConditionalStep> {
    @Override
    public void serialize(ConditionalStep conditionalStep, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField(conditionalStep.getConditionalType(),conditionalStep.getCondition());
        jsonGenerator.writeArrayFieldStart("steps");
        for (Step s : conditionalStep.getSteps()){
            jsonGenerator.writeObject(s);
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
