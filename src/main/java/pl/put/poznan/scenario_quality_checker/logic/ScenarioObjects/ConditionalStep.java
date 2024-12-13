package pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.put.poznan.scenario_quality_checker.logic.ConditionalStepSerializer;
import pl.put.poznan.scenario_quality_checker.logic.StepVisitor;

import java.util.List;

@JsonSerialize(using= ConditionalStepSerializer.class)
public class ConditionalStep implements Step {
    private ConditionalType conditionalType; // IF albo ELSE
    private String condition; // Warunek do IF lub ELSE
    private List<Step> steps; // Lista kroków wewnętrznych

    public enum ConditionalType {
        IF("IF"), ELSE("ELSE");

        ConditionalType(String label) {
        }
    }

    public ConditionalStep() { }

    // Konstruktor
    public ConditionalStep(ConditionalType conditionalType, String condition, List<Step> steps) {
        setConditionalType(conditionalType);
        setCondition(condition);
        setSteps(steps);
    }

    public String getConditionalType() {
        return conditionalType.toString();
    }
    public void setConditionalType(ConditionalType type) {
        conditionalType = type;
    }

    public void setConditionalType(String type) throws ExceptionInInitializerError {
        if (type.equals("IF")){
            this.conditionalType = ConditionalType.IF;
        } else if ((type.equals("ELSE"))) {
            this.conditionalType = ConditionalType.ELSE;
        } else {
            throw new ExceptionInInitializerError();
        }
    }

    // Getter i Setter dla pola `condition`
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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
}