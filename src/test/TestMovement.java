//package test;
//
//import javafx.scene.input.KeyCode;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import unsw.dungeon.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class TestMovement {
//
//    private Dungeon dungeon;
//
//    @BeforeEach
//    public void BeforeEach() {
//        dungeon = new Dungeon(10, 10);
//        dungeon.addEntity(new Sword(4,4));
//        dungeon.addEntity(new Treasure(5,4));
//        dungeon.addEntity(new Invincibility(6, 4));
//        dungeon.addEntity(new Key(7, 4, "0"));
//
//
//    }
//
//    @Test
//    public void TestMovementInAllDirections(){
//        Player p = new Player(0, 0, dungeon);
//
//        p.handleDirectionKey(KeyCode.RIGHT);
//        assertEquals(1, p.getX());
//        assertEquals(0, p.getY());
//
//
//        p.handleDirectionKey(KeyCode.DOWN);
//        assertEquals(1, p.getX());
//        assertEquals(1, p.getY());
//
//        p.handleDirectionKey(KeyCode.LEFT);
//        assertEquals(0, p.getX());
//        assertEquals(1, p.getY());
//
//        p.handleDirectionKey(KeyCode.UP);
//        assertEquals(0, p.getX());
//        assertEquals(0, p.getY());
//    }
//
//    @Test
//    public void TestSuccessfulPlayerCollection(){                      //TODO FIX UP THE UNIT TESTS
//        Player p = new Player(3,4, dungeon);
//        assertEquals(0, p.getSwordCount());               //Checks potion/key/sword/treasure default values
//        assertNull( p.getKey());
//        assertEquals(new DefaultPlayerState(p).getStateName(), p.getPlayerState().getStateName());
//
//        p.handleDirectionKey(KeyCode.RIGHT);                //swordCount Defaulted to -1 when no sword
//        assertEquals(Player.NUM_SWORD_SWINGS, p.getSwordCount());                 //when sword obtained, count goes to 0 and counts up to 5 when enemy is it
//
//        p.handleDirectionKey(KeyCode.RIGHT);
//        long numDeleted = dungeon.getEntities(Treasure.class)
//                .stream()
//                .map(Entity::isDeleted)
//                .filter(bool -> bool).count();
//        assertEquals(1, numDeleted);
//
//        p.handleDirectionKey(KeyCode.RIGHT);                //Checks player's state has been changed
//        assertEquals("Invincible", p.getPlayerState().getStateName());
//
//        p.handleDirectionKey(KeyCode.RIGHT);                //Checks keys field has been set properly
//        assertEquals("0", p.getKey());
//      }
//
//    }