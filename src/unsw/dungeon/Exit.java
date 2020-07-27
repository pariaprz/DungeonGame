package unsw.dungeon;

import javafx.scene.input.KeyCode;

public class Exit extends Entity {

    public static String EXIT_STATUS = "EXITED";

    public Exit(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
            setStatus(EXIT_STATUS);
        }
    }
}
