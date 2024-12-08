package pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects;

import java.util.List;

public class Scenario {
    private String title;
    private Actors actors;
    private List<SimpleStep> steps; // Kroki mogą być tekstem lub kolejną strukturą IF/FOR EACH

    // Gettery i Settery
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Actors getActors() {
        return actors;
    }
    public void setActors(Actors actors) {
        this.actors = actors;
    }

    public List<SimpleStep> getSteps() {
        return steps;
    }
    public void setSteps(List<SimpleStep> steps) {
        this.steps = steps;
    }
}

