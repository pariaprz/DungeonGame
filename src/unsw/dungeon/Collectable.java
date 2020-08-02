package unsw.dungeon;

public interface Collectable {
    boolean canUse(PlayerInventory inventory);
    void use(Player player);
}