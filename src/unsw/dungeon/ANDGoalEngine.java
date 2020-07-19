package unsw.dungeon;

import java.util.List;

/**
 * This class represents the border of the map.
 * Ensures that no entity can escape the map.
 * Pretty much identical to a wall,
 * but is a different class so wall can change without introducing new bugs
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
