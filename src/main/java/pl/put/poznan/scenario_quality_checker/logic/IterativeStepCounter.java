package pl.put.poznan.scenario_quality_checker.logic;

import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.*;


public class IterativeStepCounter {

    // Metoda główna licząca kroki warunkowe w scenariuszu
    public int countIterativeSteps(Scenario scenario) {
        if (scenario == null || scenario.getSteps() == null) {
            return 0;
        }
        return countIterativeStepsInList(scenario.getSteps());
    }

    // Metoda pomocnicza licząca kroki warunkowe rekurencyjnie
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
