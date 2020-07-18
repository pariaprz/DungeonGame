package unsw.dungeon;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * An entity in the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class Entity {

    // IntegerProperty is used so that changes to the entities position can be
    // externally observed.
    private Position position;
    private Dungeon dungeon;

    /**
     * Create an entity positioned in square (x,y)
     * @param x
     * @param y
     */
    public Entity(int x, int y, Dungeon dungeon) {
        this.position = new Position(x, y);
        this.dungeon = dungeon;
    }

    public Entity(int x, int y) {
        this(x, y, null);
    }

    public Position getPosition() {
        return position;
    }

    public int getY() {
        return position.y;
    }

    public int getX() {
        return position.x;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    Dungeon getDungeon() {
        return dungeon;
    }

    /**
     * The method to allow entities to interact with each other.
     * @param actor The entity attempting an interaction
     * @param keyCode The action attempted.
     */
    public void interact(Entity actor, KeyCode keyCode) {}
    public boolean canEntityMoveHere(Entity entity) {
        return true;
    }
}
