package pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ConditionalStep extends SimpleStep {
    @JsonProperty("IF") // Mapuje na klucz JSON "IF"
    private String condition; // Warunek IF
    private List<SimpleStep> steps; // Lista kroków wewnętrznych

    // Konstruktor
    public ConditionalStep(String description, String condition, List<SimpleStep> steps) {
        super(description); // Wywołanie konstruktora SimpleStep
        this.condition = condition;
        this.steps = steps;
    }

    // Getter i Setter dla pola `condition`
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    // Getter i Setter dla pola `steps`
    public List<SimpleStep> getSteps() {
        return steps;
    }

    public void setSteps(List<SimpleStep> steps) {
        this.steps = steps;
    }
}