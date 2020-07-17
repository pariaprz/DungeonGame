package unsw.dungeon;

import unsw.dungeon.InvincibleState;

public class DefaultState implements PlayerState{
    private String state;

    public DefaultState(){
        state = "Default";
    }

    @Override
    public void toInvincibleState(Player p){
        p.setPlayerState(new InvincibleState());
    }

    @Override
    public void toDefaultState(Player p){
    }

}