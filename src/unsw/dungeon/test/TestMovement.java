package unsw.dungeon.test;

import org.junit.Before;
import org.junit.Test;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Invincibility;
import unsw.dungeon.Key;
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
        dungeon.addEntity(new Invincibility(6, 4));
        dungeon.addEntity(new Key(7, 4, "0"));
        
        
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
    public void TestSuccessfulPlayerCollection(){                      //TODO FIX UP THE UNIT TESTS
        Player p = new Player(3,4, dungeon);
        assertEquals(-1, p.getSwordCount());               //Checks potion/key/sword/treasure default values
        assertEquals(null, p.getKey());
        assertEquals("Default", p.getPlayerState());
        assertEquals(0, p.getTreasureCount());

        p.handleDirectionKey(KeyCode.RIGHT);                //swordCount Defaulted to -1 when no sword             
        assertEquals(0, p.getSwordCount());                 //when sword obtained, count goes to 0 and counts up to 5 when enemy is it

        p.handleDirectionKey(KeyCode.RIGHT);
        assertEquals(1, p.getTreasureCount());              //Checks that the treasure count increases when picked up

        p.handleDirectionKey(KeyCode.RIGHT);                //Checks player's state has been changed
        assertEquals("Invincible", p.getPlayerState());

        p.handleDirectionKey(KeyCode.RIGHT);                //Checks keys field has been set properly
        assertEquals("0", p.getKey());
      }

    }