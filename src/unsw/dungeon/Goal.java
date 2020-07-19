package unsw.dungeon;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.beans.PropertyChangeEvent;

/**
 * This class is a wrapper for a GoalEngine.
 * It enables other classes to subscribe to a change in state.
 * attachListener() subscribes to all topics the goalEngine would like to subscribe to.
 */
public class Goal {

    private BooleanProperty isComplete = new SimpleBooleanProperty(false);

    private GoalEngine goalEngine;

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
                        entity.addObserver(entry.getValue(), (PropertyChangeEvent event) -> computeComplete(dungeon)
                ));
    }
}
