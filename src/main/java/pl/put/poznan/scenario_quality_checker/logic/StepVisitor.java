package pl.put.poznan.scenario_quality_checker.logic;

import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.ConditionalStep;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.IterativeStep;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.SimpleStep;

public interface StepVisitor {
    void visit(SimpleStep step);
    void visit(ConditionalStep step);
    void visit(IterativeStep step);
}
