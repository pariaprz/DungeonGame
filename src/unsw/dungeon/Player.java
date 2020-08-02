package unsw.dungeon;

import javafx.scene.input.KeyCode;

import java.util.List;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Player extends Moveable {

    public static String INVINCIBLE_STATUS = "invincible";
    public static String ARMED_STATUS = "armed";
    public static String DEFAULT_STATUS = "default";
    public static String WALLWALKER_STATUS = "wallwalker";
    public static String RANGER_STATUS = "ranger";

    private Direction previousDirection = Direction.RIGHT;

    private final PlayerInventory inventory;

    private PlayerState state;

    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Player(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
        this.inventory = new PlayerInventory(this);
        this.state = new DefaultPlayerState(this);
    }

    public void handleDirectionKey(KeyCode keyCode) {
        Direction direction = Direction.fromKeyCode(keyCode);
        if (direction == null) {
            return;
        }
        previousDirection = direction;
        Position nextPos = direction.fromPosition(getPosition());
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

    public PlayerInventory getInventory() {
        return inventory;
    }

    public PlayerState getPlayerState() {
        return state;
    }

    public String getPlayerStateName(){
        return state.getStateName();
    }
    
    public void setState(PlayerState playerState) {
        this.state.expireState();
        setStatus(playerState.getStateName());
        state = playerState;
        setStatus(playerState.getStateName());
    }

    public Direction getPreviousDirection() {
        return previousDirection;
    }

    public void changeWeapons() {
        inventory.cycleInventory();
    }

    public void playerDied() {
        this.delete();
    }

    public boolean attractEnemies() {
        return getPlayerState().attractsEnemies();
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Slayable) {
            getPlayerState().interactWithEnemy((Slayable) actor);
        }
    }

    public void useWeapon() {
        this.inventory.useEquipped();
    }
}
