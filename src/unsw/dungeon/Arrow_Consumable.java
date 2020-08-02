package unsw.dungeon;

import javafx.scene.input.KeyCode;

public class Arrow_Consumable extends Consumable implements Collectable {

     public Arrow_Consumable(int x, int y) {
        super(x, y);
    }
    
    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
            consume();
            ((Player) actor).getInventory().addOrIncrement(this, 1);
        }
    }

    @Override
    public boolean canUse(PlayerInventory inventory) {
        return false;
    }

    @Override
    public void use(Player player) { }
}