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
        setPosition(p);
    }

    public void moveToPosition(int x, int y) {
        moveToPosition(new Position(x, y));
    }
}
