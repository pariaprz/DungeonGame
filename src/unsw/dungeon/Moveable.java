package unsw.dungeon;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyCode;

/**
 * An entity in the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public abstract class Moveable extends Entity {

    Moveable(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }

    public void moveToPosition(Position p) {
        y().set(p.y);
        x().set(p.x);
    }

    public void moveToPosition(int x, int y) {
        y().set(y);
        x().set(x);
    }
}
