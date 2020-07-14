package unsw.dungeon.test;

import org.junit.Before;
import org.junit.Test;
import unsw.dungeon.Boulder;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;
import unsw.dungeon.Wall;

import static org.junit.Assert.assertEquals;

public class TestBoulder {

    private Dungeon dungeon;

    @Before
    public void BeforeEach() {
        dungeon = new Dungeon(10, 10);
        dungeon.addEntity(new Wall(4, 4));
    }
    @Test
    public void TestBoulderMovingOnBoulderFromAllDirections() {
        Boulder boulder = new Boulder(1, 2, dungeon);
        assertEquals(boulder.canEntityMoveHere(new Boulder(0, 2, dungeon)), false);
        assertEquals(boulder.canEntityMoveHere(new Boulder(2, 2, dungeon)), false);
        assertEquals(boulder.canEntityMoveHere(new Boulder(2, 1, dungeon)), false);
        assertEquals(boulder.canEntityMoveHere(new Boulder(2, 3, dungeon)), false);
    }

    @Test
    public void TestPlayerMovingOnBoulderFromAllDirections() {
        Boulder boulder = new Boulder(1, 2, dungeon);
        assertEquals(boulder.canEntityMoveHere(new Player(0, 2, dungeon)), true);
        assertEquals(boulder.canEntityMoveHere(new Player(2, 2, dungeon)), true);
        assertEquals(boulder.canEntityMoveHere(new Player(2, 1, dungeon)), true);
        assertEquals(boulder.canEntityMoveHere(new Player(2, 3, dungeon)), true);
    }

    @Test
    public void TestPlayerMovingOnBoulderAgainstWall() {
        assertEquals(new Boulder(3, 4, dungeon).canEntityMoveHere(new Player(2, 4, dungeon)), false);
        assertEquals(new Boulder(5, 4, dungeon).canEntityMoveHere(new Player(6, 4, dungeon)), false);
        assertEquals(new Boulder(4, 3, dungeon).canEntityMoveHere(new Player(4, 2, dungeon)), false);
        assertEquals(new Boulder(4, 5, dungeon).canEntityMoveHere(new Player(4, 6, dungeon)), false);
    }

    @Test
    public void TestPlayerMovingOnBoulderAgainstEdge() {
        assertEquals(new Boulder(0, 0, dungeon).canEntityMoveHere(new Player(0, 1, dungeon)), false);
        assertEquals(new Boulder(0, 0, dungeon).canEntityMoveHere(new Player(1, 0, dungeon)), false);
        assertEquals(new Boulder(9, 9, dungeon).canEntityMoveHere(new Player(8, 9, dungeon)), false);
        assertEquals(new Boulder(9, 9, dungeon).canEntityMoveHere(new Player(8, 8, dungeon)), false);

        assertEquals(new Boulder(0, 5, dungeon).canEntityMoveHere(new Player(1, 5, dungeon)), false);
        assertEquals(new Boulder(9, 5, dungeon).canEntityMoveHere(new Player(8, 5, dungeon)), false);
        assertEquals(new Boulder(5, 0, dungeon).canEntityMoveHere(new Player(5, 1, dungeon)), false);
        assertEquals(new Boulder(5, 9, dungeon).canEntityMoveHere(new Player(5, 8, dungeon)), false);
    }
}
