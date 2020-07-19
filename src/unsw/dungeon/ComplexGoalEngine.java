package unsw.dungeon;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the border of the map.
 * Ensures that no entity can escape the map.
 * Pretty much identical to a wall,
 * but is a different class so wall can change without introducing new bugs
 */
public abstract class ComplexGoalEngine extends GoalEngine {
    private List<Goal> childGoals;

    ComplexGoalEngine(List<Goal> childGoals) {
        super();
        this.childGoals = childGoals;
    }

    ComplexGoalEngine() {
            this(new ArrayList<>());
    }

    public void addGoal(Goal goal) {
        childGoals.add(goal);
    }

    public List<Goal> getChildGoals() {
        return childGoals;
    }

    @Override
    public List<Pair<Class<? extends Entity>, String>> getSubscriptionTopics() {
        return getChildGoals()
                .stream()
                .flatMap(goal -> goal.getGoalEngine().getSubscriptionTopics().stream())
                .collect(Collectors.toList());
    }
}
