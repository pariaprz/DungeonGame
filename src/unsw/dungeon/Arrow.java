package unsw.dungeon;

import javafx.scene.input.KeyCode;
import java.util.List;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Arrow extends Moveable {
    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Arrow(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }

    public void handleMovement(Direction direction) {
        if (direction == null) {
            return;
        }
        Position nextPos = direction.fromPosition(getPosition());
        List<Entity> entities = getDungeon().getEntitiesAt(nextPos);

        boolean canMove = entities
                .stream()
                .allMatch(entity -> entity.canEntityMoveHere(this));
        if (canMove) {
            moveToPosition(nextPos);
            entities.forEach(entity -> {
                entity.interact(this, Direction.toKeyCode(direction));
            });

        }
    }

    public void moveArrow(Direction nextDirection){
        Player player = getDungeon().getPlayer();
        if (player == null) return;
        handleMovement(nextDirection); 
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
