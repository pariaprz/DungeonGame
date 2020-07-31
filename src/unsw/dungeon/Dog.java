package unsw.dungeon;

import javafx.scene.input.KeyCode;

public class Dog extends Enemy{
    private boolean HitBefore;
    
    public Dog(int x, int y, Dungeon dungeon){
        super(x,y,dungeon);
        HitBefore = false;
    }

    public boolean getHitBefore(){
        return HitBefore;
    }

    public void setHitBefore() {
        HitBefore = true;
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player){
            if (((Player) actor).getPlayerStateName().equals("Invincible")){
                delete();
            }
        }
        if (!getHitBefore()) {
            setHitBefore();
             if (actor instanceof Arrow){
                actor.delete();
             }
        }
        else super.interact(actor, keyCode);
    }    

}