package unsw.dungeon;


import java.util.Timer;
import java.util.TimerTask;

public class InvinciblePlayerState implements PlayerState {
    private Player player;

    public InvinciblePlayerState(Player player) {
        this.player = player;
    }

    public static void startInvincibility(Player player) {
        InvinciblePlayerState newState = new InvinciblePlayerState(player);
        player.setState(newState);
        Timer timer = new Timer();
        TimerTask t = new TimerTask() {
            public void run(){
                newState.expireState();
            }
        };
        timer.schedule(t, 10*1000);                   //Time in milliseconds
    }

    @Override
    public String getStateName() {
        return "Invincible";
    }

    @Override
    public void interactWithEnemy(Enemy enemy) {
        enemy.delete();
    }

    @Override
    public void expireState() {
        this.player.setState(new DefaultPlayerState(player));
    }

    @Override
    public boolean attractsEnemies() {
        return false;
    }
}