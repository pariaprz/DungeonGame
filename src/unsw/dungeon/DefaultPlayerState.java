package unsw.dungeon;

public class DefaultPlayerState implements PlayerState {
    private Player player;
    public DefaultPlayerState(Player player) {
        this.player = player;
    }
    @Override
    public String getStateName() {
        return "Default";
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