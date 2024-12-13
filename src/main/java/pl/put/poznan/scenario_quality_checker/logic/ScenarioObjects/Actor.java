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
        Actor other = (Actor) obj;
        return name.equals(other.name) && type.equals(other.type);
    }

}
