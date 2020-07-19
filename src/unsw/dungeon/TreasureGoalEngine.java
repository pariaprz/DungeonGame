package unsw.dungeon;

import javafx.util.Pair;

import java.util.List;

/**
 * This class represents the border of the map.
 * Ensures that no entity can escape the map.
 * Pretty much identical to a wall,
 * but is a different class so wall can change without introducing new bugs
 */
public class TreasureGoalEngine extends GoalEngine {
    public TreasureGoalEngine() {
        super();
    }

    public  boolean isComplete(Dungeon dungeon) {
        return dungeon
                .getEntities()
                .stream()
                .noneMatch(entity -> entity instanceof Treasure);
    }

    @Override
    public List<Pair<Class<? extends Entity>, String>> getSubscriptionTopics() {
        return List.of(new Pair<>(Treasure.class, EntityWrapper.DELETED_EVENT));
    }
}
