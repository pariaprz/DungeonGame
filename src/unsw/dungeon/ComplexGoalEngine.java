package unsw.dungeon;

import javafx.beans.property.IntegerProperty;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Complex goals are composed of one or more smaller goals.
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

    public int getNumCompleted(Dungeon dungeon) {
        return (int)getChildGoals()
                .stream()
                .filter(goal -> goal.computeComplete(dungeon))
                .count();
    }

    @Override
    public List<Pair<Class<? extends Entity>, String>> getSubscriptionTopics() {
        return getChildGoals()
                .stream()
                .flatMap(goal -> goal.getGoalEngine().getSubscriptionTopics().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Pair<Class<? extends Entity>, IntegerProperty>> getProgressTopics() {
        return getChildGoals()
                .stream()
                .flatMap(goal -> goal.getGoalEngine().getProgressTopics().stream())
                .distinct()
                .collect(Collectors.toList());
    }
}
