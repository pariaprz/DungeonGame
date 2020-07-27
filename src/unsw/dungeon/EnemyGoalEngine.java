package unsw.dungeon;

import javafx.beans.property.IntegerProperty;
import javafx.util.Pair;

import java.util.List;

public class EnemyGoalEngine extends GoalEngine {
    public EnemyGoalEngine() {
        super();
    }

    @Override
    public  boolean isComplete(Dungeon dungeon) {
        int numAlive = dungeon.getEntities(Enemy.class).size();
        progress().setValue(numAlive);
        return numAlive == 0;
    }

    @Override
    public List<Pair<Class<? extends Entity>, String>> getSubscriptionTopics() {
        return List.of(new Pair<>(Enemy.class, EntityWrapper.DELETED_EVENT));
    }

    @Override
    public List<Pair<Class<? extends Entity>, IntegerProperty>> getProgressTopics() {
        return List.of(new Pair<>(Enemy.class, progress()));
    }
}
