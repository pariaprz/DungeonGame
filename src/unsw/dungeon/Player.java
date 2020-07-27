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
    public static int NUM_SWORD_SWINGS = 5;
    public static int ARROW_COUNT = 3;

    private int swordCount = 0;
    private int currArrowCount = 0;
    private boolean Bow = false;
    private String key = null;

    private PlayerState state;

    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Player(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
        this.state = new DefaultPlayerState(this);
    }

    public void handleDirectionKey(KeyCode keyCode) {
        Direction direction = Direction.fromKeyCode(keyCode);
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
                entity.interact(this, keyCode);
            });
        }
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean holdsKey() {
        return !(key == null);
    }

    public String getKey() {
        return key;
    }

    public void dropKey() {
        setKey(null);
    }

    public void armSword() {
        swordCount = NUM_SWORD_SWINGS;
        setStatus(ARMED_STATUS);
    }

    public boolean hasBow(){
        return Bow;
    }

    public void setBow(boolean bool){
        Bow = bool;
    }

    public void armBow(){
        currArrowCount = currArrowCount + ARROW_COUNT;
        setStatus(RANGER_STATUS);
        setBow(true);
    }

    public void pickUpArrow(){
        currArrowCount++;
        if (hasBow()) setStatus(RANGER_STATUS); 
    }

    public int getArrowCount(){
        return currArrowCount;
    }

    public void shootArrow(){
        currArrowCount--;
        if(getArrowCount() == 0) setStatus(DEFAULT_STATUS);
    }

    public boolean hasSword() {
        return swordCount > 0;
    }

    public int getSwordCount() {
        return swordCount;
    }

    public void swingSword() {
        if (!hasSword()) return;
        List<Position> areaOfEffect = List.of(
                Direction.UP.fromPosition(getPosition()),
                Direction.DOWN.fromPosition(getPosition()),
                Direction.RIGHT.fromPosition(getPosition()),
                Direction.LEFT.fromPosition(getPosition())
        );
        areaOfEffect.forEach(position ->
                getDungeon().getEntitiesAt(position).forEach(entity -> {
                    if (entity instanceof Enemy) {
                        entity.delete();
                    }
                })
        );
        swordCount--;
        if (swordCount == 0) setStatus(DEFAULT_STATUS);
    }

    public PlayerState getPlayerState() {
        return state;
    }

    public String getPlayerStateName(){
        return state.getStateName();
    }
    
    public void setState(PlayerState playerState) {
        this.state.expireState();
        if (playerState instanceof InvinciblePlayerState) {
            setStatus(INVINCIBLE_STATUS);
        } else if (hasSword()) {
            setStatus(ARMED_STATUS);
        } else if (hasBow() && currArrowCount > 0){
            setStatus(RANGER_STATUS);
        } else if (playerState instanceof WallWalkerPlayerState){
            setStatus(WALLWALKER_STATUS);
        } else {
            setStatus(DEFAULT_STATUS);
        }
        System.out.println("Setting instance: " + playerState);
        state = playerState;
    }

    public void playerDied() {
        this.delete();
    }

    public boolean attractEnemies() {
        return getPlayerState().attractsEnemies();
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Enemy) {
            getPlayerState().interactWithEnemy((Enemy) actor);
        }
    }
}
