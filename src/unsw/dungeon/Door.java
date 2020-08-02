package unsw.dungeon;

import javafx.scene.input.KeyCode;

public class Door extends Entity {
    public static String OPEN_STATUS = "open";
    final private String id;
    private boolean isUnlocked = false;
    public Door(int x, int y, String id) {
        super(x, y);
        this.id = id;
    }

    public boolean canUnlock(Key key) {
        return id.equals(key.getId());
    }

    @Override
    public boolean canEntityMoveHere(Entity entity) {
        return isUnlocked;
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Key) {
            if (id.equals(((Key) actor).getId())) {
                isUnlocked = true;
                setStatus(OPEN_STATUS);
            }
        }
    }
}
