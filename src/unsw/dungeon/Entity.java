package unsw.dungeon;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * An entity in the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class Entity {

    public static String DEFAULT_STATUS = "";

    private IntegerProperty x, y;
    private BooleanProperty isDeleted;
    private StringProperty status;
    private Dungeon dungeon;
    private PropertyChangeSupport changeSupport;

    /**
     * Create an entity positioned in square (x,y)
     * @param x
     * @param y
     */
    public Entity(int x, int y, Dungeon dungeon) {
        this.x = new SimpleIntegerProperty(x);
        this.y = new SimpleIntegerProperty(y);
        this.isDeleted = new SimpleBooleanProperty(false);
        this.status = new SimpleStringProperty(DEFAULT_STATUS);

        this.dungeon = dungeon;
        this.changeSupport = new PropertyChangeSupport(this);
    }

    public Entity(int x, int y) {
        this(x, y, null);
    }

    public BooleanProperty isDeletedProperty() {
        return isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted.get();
    }

    public void delete() {
        isDeleted.setValue(true);
    }

    public StringProperty status() {
        return status;
    }

    public void setStatus(String status) {
        this.status.setValue(status);
    }

    public IntegerProperty x() {
        return x;
    }

    public IntegerProperty y() {
        return y;
    }

    public Position getPosition() {
        return new Position(getX(), getY());
    }

    public int getY() {
        return y.get();
    }

    public int getX() {
        return x.get();
    }

    public void setPosition(Position position) {
        this.x.setValue(position.x);
        this.y.setValue(position.y);
    }

    public void setPosition(int x, int y) {
        this.x.setValue(x);
        this.y.setValue(y);
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
