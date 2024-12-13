// Klasa odpowiedzialna za liczenie kroków warunkowych
package pl.put.poznan.scenario_quality_checker.logic;

import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.ConditionalStep;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Scenario;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.SimpleStep;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.Step;

public class ConditionalStepCounter {

    // Metoda główna licząca kroki warunkowe w scenariuszu
    public int countConditionalSteps(Scenario scenario) {
        if (scenario == null || scenario.getSteps() == null) {
            return 0;
        }
        return countConditionalStepsInList(scenario.getSteps());
    }

    // Metoda pomocnicza licząca kroki warunkowe rekurencyjnie
    private int countConditionalStepsInList(java.util.List<Step> steps) {
        int count = 0;
        for (Step step : steps) {
            if (step instanceof ConditionalStep) {
                count++;
                count += countConditionalStepsInList(((ConditionalStep) step).getSteps());
            }
        }
        return count;
    }
}
