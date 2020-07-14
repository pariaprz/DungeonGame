package unsw.dungeon;

import javafx.scene.input.KeyCode;

import java.util.List;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Enemy extends Moveable {

    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Enemy(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }


    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        super.interact(actor, keyCode); // TODO: Make it work.
    }

}
