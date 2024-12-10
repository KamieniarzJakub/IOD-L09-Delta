package pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects;

public class Actor {
    public Actor(ActorType type, String name) {
        this.type = type;
        this.name = name;
    }

    private String name;

    public ActorType getType() {
        return type;
    }

    public void setType(ActorType type) {
        this.type = type;
    }

    public enum ActorType {
        EXTERNAL, SYSTEM
    }

    private ActorType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
