package unsw.dungeon;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Pair;

import java.util.List;

/**
 * This class represents the border of the map.
 * Ensures that no entity can escape the map.
 * Pretty much identical to a wall,
 * but is a different class so wall can change without introducing new bugs
 */
public abstract class GoalEngine {
    GoalEngine() { }

    public abstract List<Pair<Class<? extends Entity>, String>> getSubscriptionTopics();
    public abstract boolean isComplete(Dungeon dungeon);
}
