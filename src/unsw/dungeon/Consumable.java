package unsw.dungeon;

/**
 * An entity in the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class Consumable extends Entity {

    private boolean consumed = false;

    Consumable(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }

    public boolean isConsumed() {
       return consumed;
    }

    public void consume() {
        consumed = true;
        getDungeon().removeEntity(this);
    }
}
