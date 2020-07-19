package unsw.dungeon;

import javafx.util.Pair;

import java.util.List;

/**
 * This class represents the border of the map.
 * Ensures that no entity can escape the map.
 * Pretty much identical to a wall,
 * but is a different class so wall can change without introducing new bugs
 */
public class ExitGoalEngine extends GoalEngine {

    ExitGoalEngine() {
        super();
    }

    public  boolean isComplete(Dungeon dungeon) {
        // This must always be the last state of the game when this is the goal.
        return dungeon
                .getEntitiesAt(dungeon.getPlayer().getPosition())
                .stream()
                .anyMatch(entity -> entity instanceof Exit);
    }

    @Override
    public List<Pair<Class<? extends Entity>, String>> getSubscriptionTopics() {
        return List.of(new Pair<>(Exit.class, EntityWrapper.STATE_EVENT));
    }
}
