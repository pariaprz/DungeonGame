package unsw.dungeon;

public class DefaultPlayerState implements PlayerState{
    @Override
    public String getStateName() {
        return "Default";
    }

    @Override
    public boolean interact(Entity entity) {

        return false;
    }

    @Override
    public boolean canInteractWith(Entity entity) {
        return false;
    }
}