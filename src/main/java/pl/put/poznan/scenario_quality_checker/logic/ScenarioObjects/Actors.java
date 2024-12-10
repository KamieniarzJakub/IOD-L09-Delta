package pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects;

import java.util.List;

public class Actors {
    private List<String> external;
    private List<String> system;

    // Gettery i Settery
    public List<String> getExternal() {
        return external;
    }
    public void setExternal(List<String> external) {
        this.external = external;
    }

    public List<String> getSystem() {
        return system;
    }
    public void setSystem(List<String> system) {
        this.system = system;
    }
}
