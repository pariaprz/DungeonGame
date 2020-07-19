package unsw.dungeon;

import javafx.scene.input.KeyCode;

public class Sword extends Consumable {

    public Sword(int x, int y) {
        super(x, y);
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
            if (!((Player) actor).hasSword()) {
                consume();
                ((Player) actor).armSword();
            }
        }
    }
}
