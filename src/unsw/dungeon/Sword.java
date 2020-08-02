package unsw.dungeon;

import javafx.scene.input.KeyCode;

import java.util.List;

public class Sword extends Consumable implements Collectable {
    public static final int MAX_SWINGS = 5;
    public Sword(int x, int y) {
        super(x, y);
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
            consume();
            ((Player) actor).getInventory().addOrIncrement(
                        // Increment to MAX_SWINGS, not more.
                    this, MAX_SWINGS - ((Player) actor).getInventory().getCount(this)
            );
        }
    }

    @Override
    public boolean canUse(PlayerInventory inventory) {
        return true;
    }

    @Override
    public void use(Player player) {
        if (player.getInventory().contains(this)) {
            System.out.println("Using sword");
            List<Position> areaOfEffect = List.of(
                    Direction.UP.fromPosition(player.getPosition()),
                    Direction.DOWN.fromPosition(player.getPosition()),
                    Direction.RIGHT.fromPosition(player.getPosition()),
                    Direction.LEFT.fromPosition(player.getPosition())
            );
            areaOfEffect.forEach(position ->
                    player.getDungeon().getEntitiesAt(position).forEach(entity -> {
                        if (entity instanceof Slayable) {
                            ((Slayable) entity).registerHit();
                        }
                    }));
            player.getInventory().decrementIfExists(this);
        }
    }
}
