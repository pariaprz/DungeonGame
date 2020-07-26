package test;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import unsw.dungeon.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestBoulder {

    private Dungeon dungeon;

    @BeforeEach
    void BeforeEach() {
        dungeon = new Dungeon(10, 10);
        dungeon.addEntity(new Wall(4, 4, dungeon));
    }

    @Test
    void TestBoulderMovingOnBoulderFromAllDirections() {
        Boulder boulder = new Boulder(1, 2, dungeon);
        assertFalse(boulder.canEntityMoveHere(new Boulder(0, 2, dungeon)));
        assertFalse(boulder.canEntityMoveHere(new Boulder(2, 2, dungeon)));
        assertFalse(boulder.canEntityMoveHere(new Boulder(2, 1, dungeon)));
        assertFalse(boulder.canEntityMoveHere(new Boulder(2, 3, dungeon)));
    }

    @Test
    void TestPlayerMovingOnBoulderFromAllDirections() {
        Boulder boulder = new Boulder(1, 2, dungeon);
        assertTrue(boulder.canEntityMoveHere(new Player(0, 2, dungeon)));
        assertTrue(boulder.canEntityMoveHere(new Player(2, 2, dungeon)));
        assertTrue(boulder.canEntityMoveHere(new Player(2, 1, dungeon)));
        assertTrue(boulder.canEntityMoveHere(new Player(2, 3, dungeon)));
    }

    @Test
    void TestPlayerInteractingWithBoulderFromAllDirections() {
        Boulder boulder = new Boulder(1, 2, dungeon);

        boulder.interact(new Player(0, 2, dungeon), KeyCode.RIGHT);
        assertEquals(new Position(2, 2), boulder.getPosition());

        boulder.interact(new Player(3, 2, dungeon), KeyCode.LEFT);
        assertEquals(new Position(1, 2), boulder.getPosition());

        boulder.interact(new Player(1, 3, dungeon), KeyCode.UP);
        assertEquals(new Position(1, 1), boulder.getPosition());

        boulder.interact(new Player(1, 0, dungeon), KeyCode.DOWN);
        assertEquals(new Position(1, 2), boulder.getPosition());
    }


    //Moved To Wall Tests
    @Test
    void TestPlayerMovingOnBoulderAgainstWall() {
        assertFalse(new Boulder(3, 4, dungeon).canEntityMoveHere(new Player(2, 4, dungeon)));
        assertFalse(new Boulder(5, 4, dungeon).canEntityMoveHere(new Player(6, 4, dungeon)));
        assertFalse(new Boulder(4, 3, dungeon).canEntityMoveHere(new Player(4, 2, dungeon)));
        assertFalse(new Boulder(4, 5, dungeon).canEntityMoveHere(new Player(4, 6, dungeon)));
    }

    @Test
    void TestPlayerMovingOnBoulderAgainstEdge() {
        assertFalse(new Boulder(0, 0, dungeon).canEntityMoveHere(new Player(0, 1, dungeon)));
        assertFalse(new Boulder(0, 0, dungeon).canEntityMoveHere(new Player(1, 0, dungeon)));
        assertFalse(new Boulder(9, 9, dungeon).canEntityMoveHere(new Player(8, 9, dungeon)));
        assertFalse(new Boulder(9, 9, dungeon).canEntityMoveHere(new Player(8, 8, dungeon)));

        assertFalse(new Boulder(0, 5, dungeon).canEntityMoveHere(new Player(1, 5, dungeon)));
        assertFalse(new Boulder(9, 5, dungeon).canEntityMoveHere(new Player(8, 5, dungeon)));
        assertFalse(new Boulder(5, 0, dungeon).canEntityMoveHere(new Player(5, 1, dungeon)));
        assertFalse(new Boulder(5, 9, dungeon).canEntityMoveHere(new Player(5, 8, dungeon)));
    }
}
