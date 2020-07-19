package unsw.dungeon;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents the border of the map.
 * Ensures that no entity can escape the map.
 * Pretty much identical to a wall,
 * but is a different class so wall can change without introducing new bugs
 */
public class ANDGoalEngine extends ComplexGoalEngine {
    ANDGoalEngine(List<Goal> childGoals) {
        super(childGoals);
    }

    ANDGoalEngine() {
        super();
    }

    public boolean isComplete(Dungeon dungeon) {
        return getChildGoals().stream().allMatch(goal -> goal.isComplete(dungeon));
    }
}
