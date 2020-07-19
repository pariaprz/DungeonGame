package unsw.dungeon;

import javafx.util.Pair;

import java.util.List;

public class EnemyGoalEngine extends GoalEngine {
    public EnemyGoalEngine() {
        super();
    }

    @Override
    public  boolean isComplete(Dungeon dungeon) {
        return dungeon
                .getEntities(Enemy.class).isEmpty();
    }

    @Override
    public List<Pair<Class<? extends Entity>, String>> getSubscriptionTopics() {
        return List.of(new Pair<>(Enemy.class, EntityWrapper.DELETED_EVENT));
    }
}
