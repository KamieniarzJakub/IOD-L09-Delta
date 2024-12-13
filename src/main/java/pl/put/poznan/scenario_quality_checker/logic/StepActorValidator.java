package pl.put.poznan.scenario_quality_checker.logic;

import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StepActorValidator implements StepVisitor {

    private final List<String> actorNames;
    private final List<String> invalidSteps;
    private String currentPrefix;

    public StepActorValidator(List<Actor> externalActors, List<Actor> systemActors) {
        this.actorNames = new ArrayList<>();
        this.actorNames.addAll(externalActors.stream().map(Actor::getName).collect(Collectors.toList()));
        this.actorNames.addAll(systemActors.stream().map(Actor::getName).collect(Collectors.toList()));
        this.invalidSteps = new ArrayList<>();
        this.currentPrefix = "";
    }

    public List<String> getInvalidSteps() {
        return invalidSteps;
    }

    public static List<String> findStepsWithoutActors(Scenario scenario) {
        StepActorValidator validator = new StepActorValidator(scenario.getExternalActors(), scenario.getSystemActors());
        validateStepsWithVisitor(scenario.getSteps(), "", validator);
        return validator.getInvalidSteps();
    }

    private static void validateStepsWithVisitor(List<Step> steps, String prefix, StepActorValidator validator) {
        if (steps == null) return;

        int stepCounter = 1;
        for (Step step : steps) {
            validator.currentPrefix = prefix.isEmpty() ? String.valueOf(stepCounter) : prefix + "." + stepCounter;
            step.accept(validator);
            stepCounter++;
        }
    }

    @Override
    public void visit(SimpleStep step) {
        if (!startsWithActor(step.getDescription())) {
            invalidSteps.add(currentPrefix + ". " + step.getDescription());
        }
    }

    @Override
    public void visit(ConditionalStep step) {
//        visit(step);
        validateStepsWithVisitor(step.getSteps(), currentPrefix, this);
    }

    @Override
    public void visit(IterativeStep step) {
//        visit(step);
        validateStepsWithVisitor(step.getSteps(), currentPrefix, this);
    }

    private boolean startsWithActor(String description) {
        // Ignorowanie kroków zaczynających się od słów kluczowych
        if (description.startsWith("IF") || description.startsWith("ELSE") || description.startsWith("FOR EACH")) {
            return true;
        }
        for (String actor : actorNames) {
            if (description.startsWith(actor + " ")) {
                return true;
            }
        }
        return false;
    }
}
