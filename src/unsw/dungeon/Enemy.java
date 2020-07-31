package unsw.dungeon;

import java.util.List;

import javafx.scene.input.KeyCode;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Enemy extends Slayable {

    public static final int MAX_HEALTH = 1;
    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Enemy(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon, MAX_HEALTH);
    }

    @Override
    public int getSpeedInMillis() {
        return 500;
    }
}
