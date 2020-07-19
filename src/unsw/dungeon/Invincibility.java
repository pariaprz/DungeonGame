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
            // TODO: Add invincibility to player.
   //         ((Player) actor).addToInventory(this);                  //Check if we actually want it in inventory because we just use it and then it's gone
   //         System.out.println(((Player) actor).getInventory());    // TODO BACKEND TESTING, REMOVE LATER
            System.out.println("I AM INVINCIBLE");
            consume();

            ((Player) actor).setPlayerState(new InvinciblePlayerState());

            Timer timer = new Timer();
            TimerTask t = new TimerTask() {
                public void run(){
                    ((Player) actor).setPlayerState(new DefaultPlayerState());
                    System.out.println("No longer invincible");
                }
            };
            timer.schedule(t, 10000);                   //Time in milliseconds

        }
    }
}
