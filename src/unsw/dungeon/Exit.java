package unsw.dungeon;

import javafx.scene.input.KeyCode;

public class Exit extends Entity {

    public Exit(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
            setStatus("EXITED"); // TODO: End game.
        }
    }
}
