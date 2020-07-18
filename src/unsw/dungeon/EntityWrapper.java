package unsw.dungeon;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EntityWrapper {
    public static String POSITION_EVENT = "Position";
    public static String DELETED_EVENT = "Deleted";
    public static String STATE_EVENT = "State";

    public final Class<? extends Entity> entityClass;
    private Position position;
    private Object state;

    private final PropertyChangeSupport changeSupport;

    public EntityWrapper(Class<? extends Entity> entityClass) {
        this.entityClass = entityClass;
        this.changeSupport = new PropertyChangeSupport(this);
    }

    public void setPosition(Position position) {
        changeSupport.firePropertyChange(POSITION_EVENT, this.position, position);
        this.position = position;
    }

    public void setDeleted() {
        changeSupport.firePropertyChange(DELETED_EVENT, null, null);
    }

    public void setState(Object state) {
        changeSupport.firePropertyChange(DELETED_EVENT, this.state, state);
        this.state = state;
    }

    public void addDeleteObserver(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(DELETED_EVENT, listener);
    }
    public void addPositionObserver(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(POSITION_EVENT, listener);
    }
    public void addStateObserver(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(STATE_EVENT, listener);
    }

    public void removeDeleteObserver(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(DELETED_EVENT, listener);
    }
    public void removePositionObserver(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(POSITION_EVENT, listener);
    }
    public void removeStateObserver(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(STATE_EVENT, listener);
    }
}
