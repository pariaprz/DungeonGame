package unsw.dungeon;

import javafx.util.Pair;

import java.util.List;

/**
 * Responsible for computing the criteria for completeness.
 * It has subscription topics for entities it listens to, so that it may update whether it is complete.
 */
public abstract class GoalEngine {
    public GoalEngine() { }

    public abstract List<Pair<Class<? extends Entity>, String>> getSubscriptionTopics();
    public abstract boolean isComplete(Dungeon dungeon);
}
