package unsw.dungeon;

import javafx.scene.input.KeyCode;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Invincibility potion that sets InvinciblePlayerState on Player.
 */
public class WallWalker extends Consumable {

    public WallWalker(int x, int y) {
        super(x, y);
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
            consume();

            new WallWalkerPlayerState((Player) actor).startWallWalker();
        }
    }
}
