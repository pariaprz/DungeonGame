package unsw.dungeon;

/**
 * An entity in the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class Moveable extends Entity {

    public Moveable(int x, int y, Dungeon dungeon) {
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
