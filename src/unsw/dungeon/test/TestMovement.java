package unsw.dungeon.test;

import org.junit.Before;
import org.junit.Test;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;
import unsw.dungeon.Sword;
import unsw.dungeon.Treasure;

import javafx.scene.input.KeyCode;

import static org.junit.Assert.assertEquals;

public class TestMovement {

    private Dungeon dungeon;

    @Before
    public void BeforeEach() {
        dungeon = new Dungeon(10, 10);
        dungeon.addEntity(new Sword(4,4));
        dungeon.addEntity(new Treasure(5,4));
        
        
    }

    @Test
    public void TestMovementInAllDirections(){
        Player p = new Player(0, 0, dungeon);
       
        p.handleDirectionKey(KeyCode.RIGHT);
        assertEquals(1, p.getX());
        assertEquals(0, p.getY());

        
        p.handleDirectionKey(KeyCode.DOWN);
        assertEquals(1, p.getX());
        assertEquals(1, p.getY());

        p.handleDirectionKey(KeyCode.LEFT);
        assertEquals(0, p.getX());
        assertEquals(1, p.getY());

        p.handleDirectionKey(KeyCode.UP);
        assertEquals(0, p.getX());
        assertEquals(0, p.getY());
    }

    @Test
    public void TestInventoryCollection(){
        Player p = new Player(3,4, dungeon);
        assertEquals(0, p.getInventory().size());               //Checks inventory is empty at beginning

        p.handleDirectionKey(KeyCode.RIGHT);
        assertEquals(1, p.getInventory().size());               //Checks that the sword has successfully gone into inventory

        p.handleDirectionKey(KeyCode.RIGHT);
        assertEquals(2, p.getInventory().size());               //Checks that the treasure also made it into inventory
    }

    }