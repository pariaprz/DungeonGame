package unsw.dungeon;


public class InvincibleState implements PlayerState{
    private String state;

    public InvincibleState(){
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