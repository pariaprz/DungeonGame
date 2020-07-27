package unsw.dungeon;

import java.util.List;

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

    public void moveEnemy() {
        Player player = getDungeon().getPlayer();
        if (player == null) return;

        Direction towardsPlayer = Direction.fromPositions(getPosition(), player.getPosition());
        Direction nextDirection = player.attractEnemies() || towardsPlayer == null ?
                towardsPlayer : towardsPlayer.invert();
        handleMovement(nextDirection); // TODO: Run away.
    }


    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player){
            actor.interact(this, keyCode);
        } else if (actor instanceof Arrow){
            delete();
            actor.delete();
        }
    }
}
