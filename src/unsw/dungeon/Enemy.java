package unsw.dungeon;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.scene.input.KeyCode;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Enemy extends Moveable {

    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Enemy(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }


    public void handleMovement(KeyCode keyCode) {
    Direction direction = Direction.fromKeyCode(keyCode);
    if (direction == null) {
        return;
    }
    Position nextPos = direction.fromPosition(x().get(), y().get());
    List<Entity> entities = getDungeon().getEntitiesAt(nextPos);

    boolean canMove = entities
            .stream()
            .allMatch(entity -> entity.canEntityMoveHere(this));
    if (canMove) {
        moveToPosition(nextPos);
        entities.forEach(entity -> {
            entity.interact(this, keyCode);
            
        });

    }
    }

    public void MoveEnemy(){
        List<KeyCode> directionsList = Arrays.asList(KeyCode.LEFT, KeyCode.RIGHT, KeyCode.UP, KeyCode.DOWN);
        Random rand = new Random();

        KeyCode direction = directionsList.get(rand.nextInt(directionsList.size()));
        handleMovement(direction);
    }


    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        super.interact(actor, keyCode); // TODO: Make it work.
    }

}
