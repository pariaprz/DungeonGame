package unsw.dungeon;


public class InvinciblePlayerState implements PlayerState{
    private String state;

    public InvinciblePlayerState(){
        state = "Invincible";
    }

    @Override
    public void toInvincibleState(Player p) {
    }

    @Override
    public void toDefaultState(Player p){
        p.setPlayerState(new DefaultState());
    }
}