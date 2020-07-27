package unsw.dungeon;

import javafx.beans.property.IntegerProperty;
import javafx.util.Pair;

import java.util.List;

public class SwitchesGoalEngine extends GoalEngine {
    public SwitchesGoalEngine() {
        super();
    }

    public  boolean isComplete(Dungeon dungeon) {
        long untriggered = dungeon
                .getEntities(Switch.class)
                .stream()
                .filter(switchObj -> !switchObj.isTriggered())
                .count();
        progress().setValue(untriggered);
        return untriggered == 0;
    }

    @Override
    public List<Pair<Class<? extends Entity>, String>> getSubscriptionTopics() {
        return List.of(new Pair<>(Boulder.class, EntityWrapper.POSITION_EVENT));
    }

    @Override
    public List<Pair<Class<? extends Entity>, IntegerProperty>> getProgressTopics() {
        return List.of(new Pair<>(Switch.class, progress()));
    }
}
