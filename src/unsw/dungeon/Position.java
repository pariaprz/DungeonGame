package unsw.dungeon;

/**
 * An entity in the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class Position {
    public final int x;
    public final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof Position) {
            return ((Position) obj).x == x && ((Position) obj).y == y;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Position(" + x + ", " + y + ")";
    }
}

