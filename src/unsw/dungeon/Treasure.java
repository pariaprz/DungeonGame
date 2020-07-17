package unsw.dungeon;

import javafx.scene.input.KeyCode;

public class Treasure extends Consumable {

    public Treasure(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
            ((Player) actor).addTreasure();
            ((Player) actor).addToInventory(this);
            System.out.println(((Player) actor).getInventory());        // TODO BACKEND TESTING, REMOVE LATER
            consume();
        }
    }
}
