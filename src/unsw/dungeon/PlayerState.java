package unsw.dungeon;

public interface PlayerState {
    public void toInvincibleState(Player p);
    public void toDefaultState(Player p);
}