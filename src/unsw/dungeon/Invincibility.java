package unsw.dungeon;

import javafx.scene.input.KeyCode;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Invincibility potion that sets InvinciblePlayerState on Player.
 */
public class Invincibility extends Consumable {

    public Invincibility(int x, int y) {
        super(x, y);
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
            consume();
            new InvinciblePlayerState((Player) actor).startInvincibility();
        }
    }
}
