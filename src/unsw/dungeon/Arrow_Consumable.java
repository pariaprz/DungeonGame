package unsw.dungeon;

import javafx.scene.input.KeyCode;

public class Arrow_Consumable extends Consumable {

     public Arrow_Consumable(int x, int y) {
        super(x, y);
    }
    
    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
                consume();
                ((Player) actor).pickUpArrow();
                System.out.println("I have arrows " + ((Player) actor).getArrowCount());
        }
    }
    
}