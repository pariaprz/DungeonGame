package unsw.dungeon;

public class Switch extends Entity {

    public Switch(int x, int y) {
        super(x, y);
    }

    public boolean isTriggered() {
        return getDungeon().getEntitiesAt(getX(), getY())
                .stream()
                .anyMatch(entity -> entity instanceof Boulder);
    }
}
