package pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import pl.put.poznan.scenario_quality_checker.logic.StepVisitor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "FOR EACH", "steps" })  // Definiuje kolejność pól
public class IterativeStep implements Step {
    @JsonProperty("FOR EACH") // Mapuje na klucz JSON "FOR EACH"
    private String loopVariable; // Zmienna iteracyjna

    @JsonProperty("steps")
    private List<Step> steps; // Lista kroków wewnętrznych

    public IterativeStep() { }

    // Konstruktor
    public IterativeStep(String loopVariable, List<Step> steps) {
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
    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public void accept(StepVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null){
            return false;
        }
        if (getClass() != obj.getClass())
            return false;
        IterativeStep other = (IterativeStep) obj;
        return loopVariable.equals(other.loopVariable) && steps.equals(other.steps);
    }
}