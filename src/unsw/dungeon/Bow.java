package unsw.dungeon;

import javafx.scene.input.KeyCode;

public class Bow extends Consumable implements Collectable {
    public Bow(int x, int y) {
        super(x, y);
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
                consume();
                ((Player) actor).getInventory().addIfNotPresent(this);
                ((Player) actor).getInventory().addOrIncrement(new Arrow_Consumable(0, 0), 3);
        }
    }

    @Override
    public boolean canUse(PlayerInventory inventory) {
        return inventory.contains(new Arrow_Consumable(0, 0));
    }

    @Override
    public void use(Player player) {
        if (player.getInventory().contains(new Arrow_Consumable(0, 0))) {
            player.getInventory().decrementIfExists(new Arrow_Consumable(0, 0));

            Arrow firedArrow = new Arrow(player.getX(), player.getY(), player.getPreviousDirection(), player.getDungeon());
            player.getDungeon().createRuntimeEntity(firedArrow);
            firedArrow.setDirectionStatus();
        }
    }
}

    