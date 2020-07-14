package unsw.dungeon;

public class Switch extends Entity {

    private Dungeon dungeon;
    public Switch(int x, int y, Dungeon dungeon) {
        super(x, y);
        this.dungeon = dungeon;
    }

    public boolean isTriggered() {
        return this.dungeon.getEntitiesAt(x().get(), y().get())
                .stream()
                .anyMatch(entity -> entity instanceof Boulder);
    }
}
