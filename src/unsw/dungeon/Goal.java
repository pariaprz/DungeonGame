package unsw.dungeon;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.beans.PropertyChangeEvent;

/**
 * This class represents the border of the map.
 * Ensures that no entity can escape the map.
 * Pretty much identical to a wall,
 * but is a different class so wall can change without introducing new bugs
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
        isComplete.setValue(isComplete.get() || getGoalEngine().isComplete(dungeon));
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
                .filter(entry -> entry.getKey().equals(entity))
                .forEach(entry ->
                        entity.addObserver(entry.getValue(), (PropertyChangeEvent event) -> computeComplete(dungeon)
                ));
    }
}
