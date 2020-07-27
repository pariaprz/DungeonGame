package unsw.dungeon;

import javafx.beans.property.IntegerProperty;
import javafx.util.Pair;

import java.util.List;


public class TreasureGoalEngine extends GoalEngine {
    public TreasureGoalEngine() {
        super();
    }

    public  boolean isComplete(Dungeon dungeon) {
        int uncollected = dungeon.getEntities(Treasure.class).size();
        progress().setValue(uncollected);
        return uncollected == 0;
    }

    @Override
    public List<Pair<Class<? extends Entity>, String>> getSubscriptionTopics() {
        return List.of(new Pair<>(Treasure.class, EntityWrapper.DELETED_EVENT));
    }

    @Override
    public List<Pair<Class<? extends Entity>, IntegerProperty>> getProgressTopics() {
        return List.of(new Pair<>(Treasure.class, progress()));
    }
}
