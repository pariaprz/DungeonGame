package unsw.dungeon;

import javafx.scene.input.KeyCode;

import java.util.List;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public abstract class Slayable extends Moveable {

    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */

    private int health;
    public Slayable(int x, int y, Dungeon dungeon, int maxHealth) {
        super(x, y, dungeon);
        this.health = maxHealth;
    }

    public int getHealth(){
        return health;
    }

    public void registerHit() {
        registerHit(1);
    }

    public void registerHit(int damage) {
        this.health -= damage;
        if (health <= 0) {
            this.delete();
        }
    }

    public abstract int getSpeedInMillis();

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

    public void move() {
        Player player = getDungeon().getPlayer();
        if (player == null) return;
        Direction towardsPlayer = Direction.fromPositions(getPosition(), player.getPosition());
        Direction nextDirection = player.attractEnemies() || towardsPlayer == null ?
                towardsPlayer : towardsPlayer.invert();
        handleMovement(nextDirection); // Run away.
    }


    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player || actor instanceof Arrow){
            actor.interact(this, keyCode);
        }
    }
    
    @Override
    public boolean canEntityMoveHere(Entity entity){
        return !(entity instanceof Slayable);
    }
}
