package unsw.dungeon;

import javafx.scene.input.KeyCode;

public class Treasure extends Consumable implements Collectable {

    public Treasure(int x, int y) {
        super(x, y);
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
            ((Player) actor).getInventory().addOrIncrement(this, 1);
            consume();
        }
    }

    @Override
    public boolean canUse(PlayerInventory inventory) {
        return false;
    }

    @Override
    public void use(Player player) { }
}
