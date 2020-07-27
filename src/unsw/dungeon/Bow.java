package unsw.dungeon;

import javafx.scene.input.KeyCode;

public class Bow extends Consumable {

    public Bow(int x, int y) {
        super(x, y);
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
                consume();
                ((Player) actor).armBow();
        }
    }
}

    