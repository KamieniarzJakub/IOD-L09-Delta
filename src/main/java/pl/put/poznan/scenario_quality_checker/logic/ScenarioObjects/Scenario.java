package pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioSerializer;
import java.util.List;

@JsonSerialize(using = ScenarioSerializer.class)
public class Scenario {
    private String title;
    private List<Actor> externalActors;
    private List<Actor> systemActors ;
    private List<Step> steps; // Kroki mogą być tekstem lub kolejną strukturą IF/FOR EACH

    // Gettery i Settery
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public List<Step> getSteps() {
        return steps;
    }
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<Actor> getExternalActors() {
        return externalActors;
    }

    public void setExternalActors(List<Actor> externalActors) {
        this.externalActors = externalActors;
    }

    public List<Actor> getSystemActors() {
        return systemActors;
    }

    public void setSystemActors(List<Actor> systemActors) {
        this.systemActors = systemActors;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null){
            return false;
        }
        if (getClass() != obj.getClass())
            return false;
        Scenario other = (Scenario) obj;
        return title.equals(other.title) && externalActors.equals(other.externalActors) &&
                systemActors.equals(other.systemActors) && steps.equals(other.steps);
        }
}

