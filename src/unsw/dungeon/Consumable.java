package unsw.dungeon;

/**
 * An entity in the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class Consumable extends Entity {

    private boolean consumed = false;

    Consumable(int x, int y) {
        super(x, y);
    }

    public boolean isConsumed() {
       return consumed;
    }

    public void consume() {
        consumed = true;
        this.delete();
    }
}
