package pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.put.poznan.scenario_quality_checker.logic.ScenarioSerializer;
import java.util.List;

@JsonSerialize(using = ScenarioSerializer.class)
public class Scenario {
    /**
     * Deklaruje tytuł scenariusza. Powinien opisywać główny temat lub nazwę scenariusza.
     */
    private String title;

    /**
     * Lista aktorów zewnętrznych biorących udział w scenariuszu. Powinna zawierać obiekty klasy `Actor`.
     */
    private List<Actor> externalActors;

    /**
     * Lista aktorów systemowych biorących udział w scenariuszu.
     */
    private List<Actor> systemActors ;

    /**
     * Lista kroków definiujących akcje w scenariuszu.
     * Mogą to być tekstowe kroki lub struktury kontrolne, takie jak IF lub FOR EACH.
     */
    private List<Step> steps;

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

    /**
     * Porównuje ten scenariusz z innym obiektem, sprawdzając zgodność tytułu, aktorów i kroków.
     *
     * @param obj Obiekt do porównania
     * @return true, jeśli wszystkie atrybuty są zgodne; false w przeciwnym razie
     */
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
