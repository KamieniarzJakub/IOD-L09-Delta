package pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import pl.put.poznan.scenario_quality_checker.logic.StepVisitor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "description", "FOR EACH", "steps" })  // Definiuje kolejność pól
public class IterativeStep extends SimpleStep {
    @JsonProperty("FOR EACH") // Mapuje na klucz JSON "FOR EACH"
    private String loopVariable; // Zmienna iteracyjna

    @JsonProperty("steps")
    private List<SimpleStep> steps; // Lista kroków wewnętrznych

    public IterativeStep() { }

    // Konstruktor
    public IterativeStep(String description, String loopVariable, List<SimpleStep> steps) {
        super(description); // Wywołanie konstruktora SimpleStep
        this.loopVariable = loopVariable;
        this.steps = steps;
    }

    // Getter i Setter dla pola `loopVariable`
    public String getLoopVariable() {
        return loopVariable;
    }

    public void setLoopVariable(String loopVariable) {
        this.loopVariable = loopVariable;
    }

    // Getter i Setter dla pola `steps`
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