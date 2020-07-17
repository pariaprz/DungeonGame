package unsw.dungeon;

import javafx.scene.input.KeyCode;

import java.util.List;
import java.util.ArrayList;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Player extends Moveable {

    private int treausureCount = 0;
    private String key = null;
    private List<Consumable> inventory;

    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Player(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
        this.inventory = new ArrayList<Consumable>();
    }

    public void handleDirectionKey(KeyCode keyCode) {
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

    public List<Consumable> getInventory(){
        return inventory;
    }

    public void addToInventory(Consumable c){
        inventory.add(c);
    }

    //Removes consumable when the player no longer has the item (Sword after 5 hits, Potion after time expires)
    public void removeFromInventory(Consumable c){
        inventory.remove(c);
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        super.interact(actor, keyCode); // TODO: Change this for enemies.
    }

    public void addTreasure() {
        treausureCount++;
    }
}
