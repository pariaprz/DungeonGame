package unsw.dungeon;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;

import static javafx.scene.input.KeyCode.K;
import static javafx.scene.input.KeyCode.UP;

/**
 * The direction of a motion of an object.
 *
 */
public enum Direction {
    UP(0, 1), DOWN(0, -1), RIGHT(1, 0), LEFT(-1, 0);

    private final int x;
    private final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @param from
     * @param to
     * @return The direction from -> to
     */
    public static Direction fromPositions(Position from, Position to) {
        if (to.x > from.x) {
            return RIGHT;
        } else if (to.x < from.x) {
            return LEFT;
        } else if (to.y > from.y) {
            return UP;
        } else if (to.y < from.y) {
            return DOWN;
        }
        return null;
    }


    public Direction invert() {
        if (this.equals(LEFT)) {
            return RIGHT;
        } else if (this.equals(RIGHT)) {
            return LEFT;
        } else if (this.equals(UP)) {
            return DOWN;
        } else if (this.equals(DOWN)) {
            return UP;
        }
        return null;
    }

    public boolean equals(Direction d) {
        return d.x == this.x && d.y == this.y;
    }

    /**
     * @param x
     * @param y
     * @return The position obtained by moving 'this' direction.
     */
    public Position fromPosition(int x, int y) {
        return new Position(x + this.x, y + this.y);
    }

    /**
     *
     * @param p
     * @return The position obtained by moving 'this' direction.
     */
    public Position fromPosition(Position p) {
        return new Position(p.x + this.x, p.y + this.y);
    }

    /**
     * 'UP' represents increase in the 'y' direction.
     * 'RIGHT' represents increase in the 'x' direction.
     * @param keyCode
     * @return The direction from a keycode.
     */
    public static Direction fromKeyCode(KeyCode keyCode) {
        if (keyCode == KeyCode.UP) {
            return DOWN;
        } else if (keyCode == KeyCode.DOWN) {
            return UP;
        } else if (keyCode == KeyCode.LEFT) {
            return LEFT;
        } else if (keyCode == KeyCode.RIGHT) {
            return RIGHT;
        }
        return null;
    }

    public static KeyCode toKeyCode(Direction direction) {
        if (direction == UP) {
            return KeyCode.DOWN;
        } else if (direction == DOWN) {
            return KeyCode.UP;
        } else if (direction == LEFT) {
            return KeyCode.LEFT;
        } else if (direction == RIGHT) {
            return KeyCode.RIGHT;
        }
        return null;
    }

}

