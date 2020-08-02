//package test;
//
//import javafx.scene.input.KeyCode;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import unsw.dungeon.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class TestDoor {
//    private Dungeon dungeon;
//    private Door door1, door2;
//
//    private static final String DOOR_1_ID = "A";
//    private static final String DOOR_2_ID = "Door 2";
//    @BeforeEach
//    public void BeforeEach() {
//        dungeon = new Dungeon(10, 10);
//
//        door1 = new Door(1, 2, DOOR_1_ID);
//        door2 = new Door(7, 7, DOOR_2_ID);
//    }
//
//    @Test
//    public void TestEntitiesMovingOnUnlockedDoor() {
//        Player player = new Player(1, 1, dungeon);
//        player.setKey(DOOR_1_ID);
//        assertTrue(door1.canEntityMoveHere(player));
//        assertFalse(door2.canEntityMoveHere(player));
//        assertFalse(door1.canEntityMoveHere(new Entity(1, 2)));
//        assertFalse(door1.canEntityMoveHere(new Entity(2, 2)));
//        door1.interact(player, KeyCode.LEFT);
//        assertTrue(door1.canEntityMoveHere(player));
//        assertTrue(door1.canEntityMoveHere(new Entity(1, 2)));
//        assertTrue(door1.canEntityMoveHere(new Entity(2, 2)));
//        assertFalse(door2.canEntityMoveHere(player));
//    }
//}
//
