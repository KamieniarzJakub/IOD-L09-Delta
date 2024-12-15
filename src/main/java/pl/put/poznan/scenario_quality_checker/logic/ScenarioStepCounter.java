package pl.put.poznan.scenario_quality_checker.logic;

import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.ConditionalStep;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.IterativeStep;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.SimpleStep;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Step;

/**
 * Klasa implementująca wzorzec wizytatora do liczenia kroków w scenariuszu.
 * Obsługuje różne typy kroków: proste, warunkowe i iteracyjne.
 */
public class ScenarioStepCounter implements StepVisitor {

    /**
     * Licznik wszystkich kroków w scenariuszu.
     */
    private int stepCount = 0;

    /**
     * Flaga określająca, czy liczyć także podkroki.
     */
    private boolean countAllSteps;

    /**
     * Konstruktor tworzący licznik kroków.
     *
     * @param countAllSteps flaga określająca, czy liczyć wszystkie kroki, w tym podkroki
     */
    public ScenarioStepCounter(boolean countAllSteps) {
        this.countAllSteps = countAllSteps;
    }

    /**
     * Zwraca liczbę zliczonych kroków.
     *
     * @return liczba kroków
     */
    public int getStepCount() {
        return stepCount;
    }

    /**
     * Wizytuje prosty krok scenariusza i zwiększa licznik kroków.
     *
     * @param step krok scenariusza do odwiedzenia
     */
    @Override
    public void visit(SimpleStep step) {
        stepCount++;  // Liczy pojedynczy krok
    }

    /**
     * Wizytuje krok warunkowy scenariusza.
     * Zwiększa licznik kroków i, jeśli flaga jest aktywna, liczy również podkroki.
     *
     * @param step krok warunkowy do odwiedzenia
     */
    @Override
    public void visit(ConditionalStep step) {
        stepCount++;  // Liczy krok IF lub ELSE

        if (countAllSteps) {  // Jeśli flaga aktywna, liczy także podkroki
            for (Step innerStep : step.getSteps()) {
                innerStep.accept(this);
            }
        }
    }

    /**
     * Wizytuje krok iteracyjny scenariusza.
     * Zwiększa licznik kroków i, jeśli flaga jest aktywna, liczy również podkroki.
     *
     * @param step krok iteracyjny do odwiedzenia
     */
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
