package unsw.dungeon;

import java.util.List;

/**
 * This class represents Multiple goals AND'd together.
 * i.e. goal1 && goal2 && goal3.
 */
public class ORGoalEngine extends ComplexGoalEngine {
    public ORGoalEngine(List<Goal> childGoals) {
        super(childGoals);
    }

    public ORGoalEngine() {
            super();
    }

    public boolean isComplete(Dungeon dungeon) {
        return getNumCompleted(dungeon) > 0;
    }
}
