package unsw.dungeon;

import java.util.List;

/**
 * This class represents Multiple goals AND'd together.
 * i.e. goal1 && goal2 && goal3.
 */
public class ANDGoalEngine extends ComplexGoalEngine {
    public ANDGoalEngine(List<Goal> childGoals) {
        super(childGoals);
    }

    public ANDGoalEngine() {
        super();
    }

    public boolean isComplete(Dungeon dungeon) {
        return getChildGoals().stream().allMatch(goal -> goal.computeComplete(dungeon));
    }
}
