package unsw.dungeon.test;

import org.junit.Before;
import org.junit.Test;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Wall;
import unsw.dungeon.Boulder;
import unsw.dungeon.Player;
import unsw.dungeon.Enemy;

//import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import static org.junit.Assert.assertEquals;

public class TestWall {

    private Dungeon dungeon;

    @Before
    public void BeforeEach() {
        dungeon = new Dungeon(10, 10);
        dungeon.addEntity(new Wall(4, 4));
    }

    @Test
    public void PlayerWallInteraction(){
        Player p1 = new Player(3, 4, dungeon);      //Test movement from the left of wall into the wall
        Player p2 = new Player(5, 4, dungeon);      //Test movement from the right of wall into the wall
        Player p3 = new Player(4, 3, dungeon);      //Test movement from above the wall into the wall
        Player p4 = new Player(4, 5, dungeon);      //Test movement from below the wall into the wall

        p1.handleDirectionKey(KeyCode.RIGHT);
        assertEquals(3, p1.getX());

        p2.handleDirectionKey(KeyCode.LEFT);
        assertEquals(5, p2.getX());

        p3.handleDirectionKey(KeyCode.DOWN);
        assertEquals(3, p3.getY());

        p4.handleDirectionKey(KeyCode.UP);
        assertEquals(5, p4.getY());

    }

    @Test
    public void EnemyWallInteraction(){
        Enemy e1 = new Enemy(3, 4, dungeon);      //Test movement from the left of wall into the wall
        Enemy e2 = new Enemy(5, 4, dungeon);      //Test movement from the right of wall into the wall
        Enemy e3 = new Enemy(4, 3, dungeon);      //Test movement from above the wall into the wall
        Enemy e4 = new Enemy(4, 5, dungeon);      //Test movement from below the wall into the wall

        e1.handleMovement(KeyCode.RIGHT);
        assertEquals(3, e1.getX());

        e2.handleMovement(KeyCode.LEFT);
        assertEquals(5, e2.getX());

        e3.handleMovement(KeyCode.DOWN);
        assertEquals(3, e3.getY());

        e4.handleMovement(KeyCode.UP);
        assertEquals(5, e4.getY());
    }

    @Test
    public void BoulderWallInteraction(){
        assertEquals(new Boulder(3, 4, dungeon).canEntityMoveHere(new Player(2, 4, dungeon)), false);
        assertEquals(new Boulder(5, 4, dungeon).canEntityMoveHere(new Player(6, 4, dungeon)), false);
        assertEquals(new Boulder(4, 3, dungeon).canEntityMoveHere(new Player(4, 2, dungeon)), false);
        assertEquals(new Boulder(4, 5, dungeon).canEntityMoveHere(new Player(4, 6, dungeon)), false);
    }
}