package unsw.dungeon;

import javafx.scene.input.KeyCode;

import java.util.List;

public class Key extends Consumable implements Collectable {

    private final String id;
    public Key(int x, int y, String id) {
        super(x, y);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
            if (!((Player) actor).getInventory().contains(this)) {
                ((Player) actor).getInventory().addIfNotPresent(this);
                consume();
            }
        }
    }

    @Override
    public boolean canUse(PlayerInventory inventory) {
        return true;
    }

    @Override
    public void use(Player player) {
        List<Position> areaOfEffect = List.of(
                Direction.UP.fromPosition(player.getPosition()),
                Direction.DOWN.fromPosition(player.getPosition()),
                Direction.RIGHT.fromPosition(player.getPosition()),
                Direction.LEFT.fromPosition(player.getPosition())
        );
        areaOfEffect.forEach(position ->
                player.getDungeon().getEntitiesAt(position).forEach(entity -> {
                    if (entity instanceof Door && ((Door) entity).canUnlock(this)) {
                        entity.interact(this, KeyCode.LEFT);
                        player.getInventory().popItemIfExists(this);
                    }
                })
        );
    }
}
