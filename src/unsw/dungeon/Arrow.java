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
    final static String UP = Direction.DOWN.toString();
    final static String DOWN = Direction.UP.toString();
    final static String RIGHT = Direction.RIGHT.toString();
    final static String LEFT = Direction.LEFT.toString();

    private final Direction direction;

    public Arrow(int x, int y, Direction direction, Dungeon dungeon){
        super(x,y, dungeon);
        this.direction = direction;
    }

    public void handleMovement() {
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

        } else {
            this.delete();
        }
    }

    public void moveArrow(){
        Player player = getDungeon().getPlayer();
        if (player == null) return;

        handleMovement();
    }

    public void setDirectionStatus() {
        setStatus(direction.toString());
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {  
        if (actor instanceof Player){
            System.out.println("HELLO THERE");
        } 
    }
}
