package pl.put.poznan.scenario_quality_checker.logic;

import pl.put.poznan.scenario_quality_checker.logic.ScenarioObjects.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa walidująca kroki scenariusza pod kątem poprawności użycia aktorów.
 * Sprawdza, czy opisy kroków zaczynają się od nazw aktorów.
 */
public class StepActorValidator implements StepVisitor {

    /**
     * Lista nazw wszystkich aktorów (zewnętrznych i systemowych).
     */
    private final List<String> actorNames;

    /**
     * Lista niepoprawnych kroków (tych bez odpowiednich aktorów).
     */
    private final List<String> invalidSteps;

    /**
     * Aktualny prefiks numeru kroku w hierarchii.
     */
    private String currentPrefix;

    /**
     * Lista wszystkich kroków scenariusza
     */
    private List<Step> steps;

    /**
     * Konstruktor klasy.
     * Inicjalizuje listy aktorów oraz listę wszystkich kroków.
     * Nie inicjalizuje listy niepoprawnych kroków.
     *
     * @param scenario obiekt scenariusza do sprawdzenia
     */
    public StepActorValidator(Scenario scenario){
        this.actorNames = new ArrayList<>();
        this.actorNames.addAll(scenario.getExternalActors().stream().map(Actor::getName).collect(Collectors.toList()));
        this.actorNames.addAll(scenario.getSystemActors().stream().map(Actor::getName).collect(Collectors.toList()));
        this.steps = scenario.getSteps();
        this.invalidSteps = new ArrayList<>();
        this.currentPrefix = "";
    }

    /**
     * Zwraca listę niepoprawnych kroków.
     * Nie przeszukuje listy kroków, wyłącznie zwraca obecnie zapisaną wartość.
     *
     * @return lista kroków bez aktorów
     */
    public List<String> getInvalidSteps() {
        return invalidSteps;
    }

    /**
     * Znajduje kroki bez aktorów w scenariuszu.
     *
     * @return lista opisów niepoprawnych kroków
     */
    public List<String> findStepsWithoutActors() {
        validateStepsWithVisitor(steps, "");
        return getInvalidSteps();
    }

    /**
     * Weryfikuje kroki scenariusza z użyciem prefiksu numeru kroku.
     *
     * @param steps lista kroków do sprawdzenia
     * @param prefix prefiks określający numerację kroków
     */
    private void validateStepsWithVisitor(List<Step> steps, String prefix) {
        if (steps == null) return;

        int stepCounter = 1;
        for (Step step : steps) {
            currentPrefix = prefix.isEmpty() ? String.valueOf(stepCounter) : prefix + "." + stepCounter;
            step.accept(this);
            stepCounter++;
        }
    }

    /**
     * Wizytuje prosty krok scenariusza i sprawdza, czy zaczyna się od nazwy aktora.
     *
     * @param step prosty krok do sprawdzenia
     */
    public void visit(SimpleStep step) {
        if (!startsWithActor(step.getDescription())) {
            invalidSteps.add(currentPrefix + ". " + step.getDescription());
        }
    }

    /**
     * Wizytuje krok warunkowy scenariusza i sprawdza jego podkroki.
     *
     * @param step krok warunkowy do sprawdzenia
     */
    public void visit(ConditionalStep step) {
        validateStepsWithVisitor(step.getSteps(), currentPrefix);
    }

    /**
     * Wizytuje krok iteracyjny scenariusza i sprawdza jego podkroki.
     *
     * @param step krok iteracyjny do sprawdzenia
     */
    public void visit(IterativeStep step) {
        validateStepsWithVisitor(step.getSteps(), currentPrefix);
    }

    /**
     * Sprawdza, czy opis kroku zaczyna się od nazwy aktora.
     *
     * @param description opis kroku
     * @return true, jeśli opis zaczyna się od aktora lub słowa kluczowego
     */
    private boolean startsWithActor(String description) {
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
