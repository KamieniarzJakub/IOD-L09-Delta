package pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.put.poznan.scenario_quality_checker.logic.SimpleStepSerializer;
import pl.put.poznan.scenario_quality_checker.logic.StepVisitor;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleStep {
    private String description; // Opis kroku

    public SimpleStep() { }

    // Konstruktor
    public SimpleStep(String description) {
        this.description = description;
    }

    // Getter i Setter
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void accept(StepVisitor visitor) {
        visitor.visit(this);
    }
}