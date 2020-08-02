package unsw.dungeon;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Entity consumed only by the player. Makes the player invincible.
 * It is deleted once it is consumed.
 */
public class InvinciblePlayerState extends PlayerState {
    private final Player player;
    private Timer taskTimer;

    public static String STATE_NAME = "Invincible";
    public static int MAX_TIME = 5;

    public InvinciblePlayerState(Player player) {
        this.player = player;
    }

    public void startInvincibility() {
        setState();
        player.setState(this);
        taskTimer = new Timer();
        PlayerState.scheduleState(this, taskTimer, MAX_TIME);
    }

    @Override
    public String getStateName() {
        return STATE_NAME;
    }

    @Override
    public void interactWithEnemy(Slayable enemy) {
        enemy.registerHit(enemy.getHealth()); // Kills them.
    }

    @Override
    public void expireState() {
        if (this.taskTimer != null) {
            this.taskTimer.cancel();
            this.taskTimer = null;
            this.player.setState(new DefaultPlayerState(player));
        }
    }

    @Override
    public boolean attractsEnemies() {
        return false;
    }
}