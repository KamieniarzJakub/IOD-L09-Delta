package pl.put.poznan.scenario_quality_checker.logic;

import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.ConditionalStep;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.IterativeStep;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.SimpleStep;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Step;

public class ScenarioStepCounter implements StepVisitor {
    private int stepCount = 0;
    private boolean countAllSteps;  // Flaga określająca tryb liczenia

    // Konstruktor przyjmujący tryb liczenia
    public ScenarioStepCounter(boolean countAllSteps) {
        this.countAllSteps = countAllSteps;
    }

    // Getter do uzyskania wyniku liczenia
    public int getStepCount() {
        return stepCount;
    }

    // Wizytacja prostego kroku
    @Override
    public void visit(SimpleStep step) {
        stepCount++;  // Liczy pojedynczy krok
    }

    // Wizytacja kroku warunkowego
    @Override
    public void visit(ConditionalStep step) {
        stepCount++;  // Liczy krok IF lub ELSE

        if (countAllSteps) {  // Jeśli flaga aktywna, liczy także podkroki
            for (Step innerStep : step.getSteps()) {
                innerStep.accept(this);
            }
        }
    }

    // Wizytacja kroku iteracyjnego
    @Override
    public void visit(IterativeStep step) {
        stepCount++;  // Liczy krok FOR EACH

        if (countAllSteps) {  // Jeśli flaga aktywna, liczy także podkroki
            for (Step innerStep : step.getSteps()) {
                innerStep.accept(this);
            }
        }
    }
}

