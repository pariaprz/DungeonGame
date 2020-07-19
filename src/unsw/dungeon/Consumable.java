package unsw.dungeon;

/**
 * An entity in the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class Consumable extends Entity {

    Consumable(int x, int y) {
        super(x, y);
    }

    public void consume() {
        this.delete();
    }
}
