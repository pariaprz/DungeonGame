package unsw.dungeon;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;

/**
 * This class is used to provide updates to any listeners.
 * This is intended to supply any updates approved by the controller to any subscribers.
 */
public class EntityWrapper {
    public static String POSITION_EVENT = "Position";
    public static String DELETED_EVENT = "Deleted";
    public static String STATUS_EVENT = "State";

    public final Class<? extends Entity> entityClass;
    private Position position;

    private final PropertyChangeSupport changeSupport;

    public EntityWrapper(Entity entity) {
        this.entityClass = entity.getClass();
        this.position = entity.getPosition();
        this.changeSupport = new PropertyChangeSupport(this);
    }

    public void setPosition(Position position) {
        changeSupport.firePropertyChange(POSITION_EVENT, this.position, position);
        this.position = position;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setDeleted() {
        changeSupport.firePropertyChange(DELETED_EVENT, null, null);
    }

    public void publishStatusUpdate(String status) {
        changeSupport.firePropertyChange(STATUS_EVENT, null, status);
    }

    public void addDeleteObserver(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(DELETED_EVENT, listener);
    }
    public void addPositionObserver(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(POSITION_EVENT, listener);
    }
    public void addStatusObserver(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(STATUS_EVENT, listener);
    }

    public void addObserver(String topic, PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(topic, listener);
    }

    public void unsubscribeDelete(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(DELETED_EVENT, listener);
    }
    public void unsubscribePosition(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(POSITION_EVENT, listener);
    }
    public void unsubscribeState(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(STATUS_EVENT, listener);
    }
    public void dropAllSubscribers() {
        Arrays.stream(this.changeSupport.getPropertyChangeListeners(DELETED_EVENT))
                .forEach(this::unsubscribeDelete);
        Arrays.stream(this.changeSupport.getPropertyChangeListeners(STATUS_EVENT))
                .forEach(this::unsubscribeState);
        Arrays.stream(this.changeSupport.getPropertyChangeListeners(POSITION_EVENT))
                .forEach(this::unsubscribePosition);
    }

    @Override
    public String toString() {
        return "EntityWrapper{" +
                "entityClass=" + entityClass +
                ", position=" + position +
                '}';
    }
}
