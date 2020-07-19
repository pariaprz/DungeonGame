package unsw.dungeon;

public class DefaultPlayerState implements PlayerState {
    public static String STATE_NAME = "Default";

    private Player player;
    public DefaultPlayerState(Player player) {
        this.player = player;
    }
    @Override
    public String getStateName() {
        return STATE_NAME;
    }

    @Override
    public void interactWithEnemy(Enemy enemy) {
        player.playerDied();
    }

    @Override
    public void expireState() { }

    @Override
    public boolean attractsEnemies() {
        return true;
    }
}