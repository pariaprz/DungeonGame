package unsw.dungeon;


import java.util.Timer;
import java.util.TimerTask;

/**
 * Entity consumed only by the player. Makes the player be able to walk through walls.
 * It is deleted once it is consumed.
 */
public class WallWalkerPlayerState extends PlayerState {
    private final Player player;
    private Timer taskTimer;

    public static String STATE_NAME = "Wallwalker";
    public static int MAX_TIME = 3;
    public WallWalkerPlayerState(Player player) {
        this.player = player;
    }

    public void startWallWalker() {
        player.setState(this);
        taskTimer = new Timer();
        setState();
        PlayerState.scheduleState(this, taskTimer, MAX_TIME);
    }

    @Override
    public String getStateName() {
        return STATE_NAME;
    }


    @Override
    public void interactWithEnemy(Slayable enemy) {
        player.playerDied();
    }

    @Override
    public boolean attractsEnemies() {
        return false;
    }
    @Override
    public void expireState() {
        if (this.taskTimer != null) {
            this.taskTimer.cancel();
            this.taskTimer = null;
            this.player.setState(new DefaultPlayerState(player));
        }
    }

}