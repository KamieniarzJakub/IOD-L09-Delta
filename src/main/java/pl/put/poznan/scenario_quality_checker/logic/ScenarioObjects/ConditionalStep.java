package pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.put.poznan.scenario_quality_checker.logic.StepVisitor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConditionalStep extends SimpleStep {
    private String condition; // Warunek IF lub ELSE
    private List<SimpleStep> steps; // Lista kroków wewnętrznych

    public ConditionalStep() { }

    // Konstruktor
    public ConditionalStep(String description, String condition, List<SimpleStep> steps) {
        super(description); // Wywołanie konstruktora SimpleStep
        this.condition = condition;
        this.steps = steps;
    }

    /**
     * Dynamiczne mapowanie JSON słowa kluczowego
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getConditionalType() {
        if (super.getDescription() != null) {
            if (super.getDescription().toUpperCase().startsWith("IF")) {
                return "IF";
            } else if (super.getDescription().toUpperCase().startsWith("ELSE")) {
                return "ELSE";
            }
        }
        return "";
    }

    // Getter i Setter dla pola `condition`
    @JsonProperty("condition")
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    // Getter i Setter dla pola `steps`
    @JsonProperty("steps")
    public List<SimpleStep> getSteps() {
        return steps;
    }

    public void setSteps(List<SimpleStep> steps) {
        this.steps = steps;
    }

    @Override
    public void accept(StepVisitor visitor) {
        visitor.visit(this);
    }
}