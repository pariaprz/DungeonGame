package unsw.dungeon;

import javafx.scene.input.KeyCode;

public class Key extends Consumable {

    private String id;
    public Key(int x, int y, String id) {
        super(x, y);
        this.id = id;
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
            if (!((Player) actor).holdsKey()) {
                ((Player) actor).setKey(this.id);
      //          ((Player) actor).addToInventory(this);
      //          System.out.println(((Player) actor).getInventory());    // TODO BACKEND TESTING, REMOVE LATER
                consume();
            }
        }
    }
}
