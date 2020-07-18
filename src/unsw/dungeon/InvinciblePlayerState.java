package unsw.dungeon;


public class InvinciblePlayerState implements PlayerState {
    private String state;

    public InvinciblePlayerState(){
        state = "Invincible";
    }

    @Override
    public String getStateName() {
        return state;
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