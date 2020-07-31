package unsw.dungeon;

import javafx.scene.input.KeyCode;

public class Dog extends Slayable {
    private static final int MAX_HEALTH = 2;
    
    public Dog(int x, int y, Dungeon dungeon){ super(x,y,dungeon, MAX_HEALTH); }

    @Override
    public int getSpeedInMillis() {
        return 250;
    }
}