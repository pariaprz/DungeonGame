package unsw.dungeon;

import javafx.scene.input.KeyCode;
import java.util.Timer;
import java.util.TimerTask;

public class Invincibility extends Consumable {

    public Invincibility(int x, int y) {
        super(x, y);
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
            System.out.println("I AM INVINCIBLE");
            consume();

            InvinciblePlayerState.startInvincibility((Player) actor);
        }
    }
}
