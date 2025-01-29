// Klasa odpowiedzialna za liczenie kroków warunkowych
package pl.put.poznan.scenario_quality_checker.logic;

import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.*;

/**
 * Klasa odpowiedzialna za liczenie kroków warunkowych w scenariuszu.
 */
public class ConditionalStepCounter {

    /**
     * Liczy liczbę kroków warunkowych w danym scenariuszu.
     *
     * @param scenario Scenariusz, w którym będą liczone kroki warunkowe.
     * @return Liczba znalezionych kroków warunkowych.
     */
    public int countConditionalSteps(Scenario scenario) {
        if (scenario == null || scenario.getSteps() == null) {
            return 0;
        }
        return countConditionalStepsInList(scenario.getSteps());
    }

    /**
     * Rekurencyjnie liczy kroki warunkowe w liście kroków scenariusza.
     *
     * @param steps Lista kroków do analizy.
     * @return Liczba kroków warunkowych w podanych krokach.
     */
    private int countConditionalStepsInList(java.util.List<Step> steps) {
        int count = 0;

        for (Step step : steps) {
            if (step instanceof ConditionalStep) {
                count++;
                count += countConditionalStepsInList(((ConditionalStep) step).getSteps());
            } else if (step instanceof IterativeStep) {
                count += countConditionalStepsInList(((IterativeStep) step).getSteps());
            }
        }
        return count;
    }
}