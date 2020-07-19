package unsw.dungeon;

import javafx.scene.input.KeyCode;

public class Portal extends Entity {

    private Portal linkedPortal;
    private String id;
    public Portal(int x, int y, String id) {
        super(x, y);
        this.id = id;
    }

    public Portal(int x, int y, String id, Portal linkedPortal) {
        super(x, y);
        this.id = id;
        this.linkedPortal = linkedPortal;
    }

    public void setLinkedPortal(Portal linkedPortal) {
        this.linkedPortal = linkedPortal;
    }

    public Portal getLinkedPortal() {
        return linkedPortal;
    }
    
    public String getPortalID() {
        return id;
    }

    
    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (linkedPortal == null) {
            throw new RuntimeException("No linked portal for ID: " + id);
        }
        if (actor instanceof Moveable) {
            ((Moveable) actor).moveToPosition(linkedPortal.getX(), linkedPortal.getY());
        }
    }
}
