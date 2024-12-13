package pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects;

import pl.put.poznan.scenario_quality_checker.logic.StepVisitor;

public interface Step {
    void accept(StepVisitor visitor);
}
