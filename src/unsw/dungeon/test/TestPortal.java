package unsw.dungeon.test;

import org.junit.Before;
import org.junit.Test;
import unsw.dungeon.*;

import static org.junit.Assert.assertEquals;

public class TestPortal {

    private Dungeon dungeon;
    private Portal portal1;
    private Portal portal2;

    @Before
    public void BeforeEach() {
        dungeon = new Dungeon(10, 10);
        portal1 = new Portal(1, 2, "A");
        portal2 = new Portal(7, 7, "A");
        portal2.setLinkedPortal(portal1);
        portal1.setLinkedPortal(portal2);
    }

    @Test
    public void TestEntitiesMovingOnPortalFromAllDirections() {
        assertEquals(portal1.canEntityMoveHere(new Entity(0, 2)), true);
        assertEquals(portal1.canEntityMoveHere(new Entity(2, 2)), true);
        assertEquals(portal1.canEntityMoveHere(new Entity(2, 1)), true);
        assertEquals(portal1.canEntityMoveHere(new Entity(2, 3)), true);
    }

    @Test(expected = RuntimeException.class)
    public void TestUnlinkedPortal() {
        Portal portal = new Portal(0, 0, "P");
        portal.interact(new Entity(0, 0), null);
    }

    @Test
    public void TestPortalInteractionWithEntity() {
        Entity entity = new Entity(portal1.getX(), portal1.getY());
        portal1.interact(entity, null);
        assertEquals(entity.getX(), portal1.getX());
        assertEquals(entity.getY(), portal1.getY());

        entity = new Entity(portal2.getX(), portal2.getY());
        portal2.interact(entity, null);
        assertEquals(entity.getX(), portal2.getX());
        assertEquals(entity.getY(), portal2.getY());
    }

    @Test
    public void TestPortalInteractionWithMoveable() {
        Moveable moveable = new Moveable(portal1.getX(), portal1.getY(), dungeon);
        portal1.interact(moveable, null);
        assertEquals(moveable.getX(), portal2.getX());
        assertEquals(moveable.getY(), portal2.getY());

        portal2.interact(moveable, null);
        assertEquals(moveable.getX(), portal1.getX());
        assertEquals(moveable.getY(), portal1.getY());
    }
}
