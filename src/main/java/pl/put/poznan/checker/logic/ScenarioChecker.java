package pl.put.poznan.checker.logic;


public class ScenarioChecker {

    private final String[] transforms;

    public ScenarioChecker(String[] transforms){
        this.transforms = transforms;
    }

    public String transform(String text){
        // of course, normally it would do something based on the transforms
        return text.toUpperCase();
    }
}
