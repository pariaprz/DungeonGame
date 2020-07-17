package unsw.dungeon;

import javafx.scene.input.KeyCode;
import java.util.Timer;
import java.util.TimerTask;

public class Invincibility extends Consumable {

    public Invincibility(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
            // TODO: Add invincibility to player.
            ((Player) actor).addToInventory(this);
            System.out.println(((Player) actor).getInventory());    // TODO BACKEND TESTING, REMOVE LATER
            System.out.println("I AM INVINCIBLE");
            consume();

            ((Player) actor).getPlayerState().toInvincibleState((Player) actor);
            Timer timer = new Timer();
            TimerTask t = new TimerTask() {
                public void run(){
                    ((Player) actor).getPlayerState().toDefaultState((Player) actor);
                    System.out.println("No longer invincible");
                }
            };
            timer.schedule(t, 10000);                   //Time in milliseconds
                
        }
    }
}
