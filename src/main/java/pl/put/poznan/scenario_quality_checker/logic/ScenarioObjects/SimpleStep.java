package pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects;

public class SimpleStep {
    private String description; // Opis kroku

    // Konstruktor
    public SimpleStep(String description) {
        this.description = description;
    }

    // Getter i Setter
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}