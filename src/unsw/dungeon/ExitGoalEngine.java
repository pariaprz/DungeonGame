package unsw.dungeon;

import javafx.beans.property.IntegerProperty;
import javafx.util.Pair;

import java.util.List;

public class ExitGoalEngine extends GoalEngine {

    public ExitGoalEngine() {
        super();
        progress().setValue(0);
    }

    public  boolean isComplete(Dungeon dungeon) {
        // This must always be the last state of the game when this is the goal.
        boolean isComplete = dungeon
                .getEntitiesAt(dungeon.getPlayer().getPosition())
                .stream()
                .anyMatch(entity -> entity instanceof Exit);
        progress().setValue(isComplete ? 1 : 0);
        return isComplete;
    }

    @Override
    public List<Pair<Class<? extends Entity>, String>> getSubscriptionTopics() {
        return List.of(new Pair<>(Player.class, EntityWrapper.POSITION_EVENT));
    }

    @Override
    public List<Pair<Class<? extends Entity>, IntegerProperty>> getProgressTopics() {
        return List.of(new Pair<>(Exit.class, progress()));
    }
}
