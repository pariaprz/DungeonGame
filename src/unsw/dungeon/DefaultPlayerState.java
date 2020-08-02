package unsw.dungeon;

public class DefaultPlayerState extends PlayerState {
    public final static String STATE_NAME = "Default";

    private Player player;
    public DefaultPlayerState(Player player) {
        this.player = player;
        setState();
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
    public void expireState() { }

    @Override
    public boolean attractsEnemies() {
        return true;
    }
}