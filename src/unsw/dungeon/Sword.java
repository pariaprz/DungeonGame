package unsw.dungeon;

import javafx.scene.input.KeyCode;

public class Sword extends Consumable {

    public Sword(int x, int y) {
        super(x, y);
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
            if (((Player) actor).getSwordCount() == -1){
                consume();
                ((Player) actor).setSwordIncrement();
            } 
       //     try {
       //         ((Player) actor).getSwordCount() ;
       //     } catch (NullPointerException e) {
       //         consume();
       //         ((Player) actor).addToInventory(this);
        //        ((Player) actor).setSwordIncrement();
       //     }
            
           // System.out.println(((Player) actor).getInventory());        // TODO BACKEND TESTING, REMOVE LATER
            
        }
    }
}
