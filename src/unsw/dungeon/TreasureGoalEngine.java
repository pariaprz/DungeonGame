package unsw.dungeon;

import javafx.util.Pair;

import java.util.List;


public class TreasureGoalEngine extends GoalEngine {
    public TreasureGoalEngine() {
        super();
    }

    public  boolean isComplete(Dungeon dungeon) {
        return dungeon
                .getEntities(Treasure.class).isEmpty();
    }

    @Override
    public List<Pair<Class<? extends Entity>, String>> getSubscriptionTopics() {
        return List.of(new Pair<>(Treasure.class, EntityWrapper.DELETED_EVENT));
    }
}
