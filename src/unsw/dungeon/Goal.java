package unsw.dungeon;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is a wrapper for a GoalEngine.
 * It enables other classes to subscribe to a change in state.
 * attachListener() subscribes to all topics the goalEngine would like to subscribe to.
 */
public class Goal {

    private final BooleanProperty isComplete = new SimpleBooleanProperty(false);

    private final GoalEngine goalEngine;

    public Goal(GoalEngine engine) {
        this.goalEngine = engine;
    }

    public GoalEngine getGoalEngine() {
        return goalEngine;
    }

    public boolean computeComplete(Dungeon dungeon) {
        isComplete.setValue(getGoalEngine().isComplete(dungeon));
        return isComplete.get();
    }

    public boolean isComplete() {
        return isComplete.get();
    }

    public BooleanProperty getCompleteProperty() {
        return isComplete;
    }

    public void attachListener(EntityWrapper entity, Dungeon dungeon) {
        getGoalEngine()
                .getSubscriptionTopics()
                .stream()
                .filter(entry -> entry.getKey().equals(entity.entityClass))
                .forEach(entry ->
                        entity.addObserver(entry.getValue(), (event) -> computeComplete(dungeon)
                ));
    }

    public List<Pair<Class<? extends Entity>, IntegerProperty>> observableTopics() {
        return getGoalEngine().getProgressTopics();
    }
}
