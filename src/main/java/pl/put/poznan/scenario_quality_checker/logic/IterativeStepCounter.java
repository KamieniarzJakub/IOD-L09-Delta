package pl.put.poznan.scenario_quality_checker.logic;

import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.*;

/**
 * Klasa odpowiedzialna za liczenie kroków iteracyjnych w scenariuszu.
 */
public class IterativeStepCounter {
    /**
     * Liczy liczbę kroków iteracyjnych w danym scenariuszu.
     *
     * @param scenario Scenariusz, w którym będą liczone kroki iteracyjne.
     * @return Liczba znalezionych kroków iteracyjnych.
     */
    public int countIterativeSteps(Scenario scenario) {
        if (scenario == null || scenario.getSteps() == null) {
            return 0;
        }
        return countIterativeStepsInList(scenario.getSteps());
    }

    /**
     * Rekurencyjnie liczy kroki iteracyjne w liście kroków scenariusza.
     *
     * @param steps Lista kroków do analizy.
     * @return Liczba kroków iteracyjnych w podanych krokach.
     */
    private int countIterativeStepsInList(java.util.List<Step> steps) {
        int count = 0;

        for (Step step : steps) {
            if (step instanceof IterativeStep) {
                count++;
                count += countIterativeStepsInList(((IterativeStep) step).getSteps());
            } else if (step instanceof ConditionalStep) {
                count += countIterativeStepsInList(((ConditionalStep) step).getSteps());
            }
        }
        return count;
    }
}
