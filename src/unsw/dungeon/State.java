package unsw.dungeon;

public interface State {
    public boolean interact(Entity entity);
    public boolean canInteractWith(Entity entity);
    public String getStateName();
}