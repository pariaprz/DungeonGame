package unsw.dungeon;

import javafx.scene.input.KeyCode;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Boulder extends Moveable {
    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Boulder(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }

    @Override
    public boolean canEntityMoveHere(Entity entity) {
        if (!(entity instanceof Player)) return false;

        Direction pushDirection = Direction.fromPositions(entity.getPosition(), getPosition());
        if (pushDirection == null) return false;
        return getDungeon().getEntitiesAt(pushDirection.fromPosition(getPosition()))
                .stream()
                .allMatch(e -> e.canEntityMoveHere(this));
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        Direction movementDirection = Direction.fromKeyCode(keyCode);
        if (actor instanceof Player && movementDirection != null) {
            Position newPosition = movementDirection.fromPosition(getPosition());
            moveToPosition(newPosition);
            getDungeon().getEntitiesAt(getPosition()).forEach(entity -> entity.interact(this, keyCode));
        }
    }
}
