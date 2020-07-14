package unsw.dungeon;

public class Switch extends Entity {

    public Switch(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }

    public boolean isTriggered() {
        return getDungeon().getEntitiesAt(x().get(), y().get())
                .stream()
                .anyMatch(entity -> entity instanceof Boulder);
    }
}
