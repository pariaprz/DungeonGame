package unsw.dungeon;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Pair;

import java.util.List;

/**
 * Responsible for computing the criteria for completeness.
 * It has subscription topics for entities it listens to, so that it may update whether it is complete.
 */
public abstract class GoalEngine {
    private final IntegerProperty progress;

    public GoalEngine() {
        progress = new SimpleIntegerProperty();
    }

    public abstract List<Pair<Class<? extends Entity>, String>> getSubscriptionTopics();
    public abstract List<Pair<Class<? extends Entity>, IntegerProperty>> getProgressTopics();
    public IntegerProperty progress() {
        return progress;
    }
    public abstract boolean isComplete(Dungeon dungeon);
}
