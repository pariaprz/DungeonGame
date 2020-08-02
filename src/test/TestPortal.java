// package test;


// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestInstance;
// import unsw.dungeon.Dungeon;
// import unsw.dungeon.Entity;
// import unsw.dungeon.Moveable;
// import unsw.dungeon.Portal;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertThrows;

// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
// public class TestPortal {

//     private Dungeon dungeon;
//     private Portal portal1;
//     private Portal portal2;

//     @BeforeEach
//     public void BeforeEach() {
//         dungeon = new Dungeon(10, 10);
//         portal1 = new Portal(1, 2, "A");
//         portal2 = new Portal(7, 7, "A");
//         portal2.setLinkedPortal(portal1);
//         portal1.setLinkedPortal(portal2);
//     }

//     @Test
//     public void TestEntitiesMovingOnPortalFromAllDirections() {
//         assertEquals(portal1.canEntityMoveHere(new Entity(0, 2)), true);
//         assertEquals(portal1.canEntityMoveHere(new Entity(2, 2)), true);
//         assertEquals(portal1.canEntityMoveHere(new Entity(2, 1)), true);
//         assertEquals(portal1.canEntityMoveHere(new Entity(2, 3)), true);
//     }

//     @Test
//     public void TestLinkedPortals() {
//         assertEquals(portal1.getPortalID(), portal2.getPortalID());
//         assertEquals(portal1.getLinkedPortal(), portal2);
//         assertEquals(portal2.getLinkedPortal(), portal1);
//     }

//     @Test
//     public void TestUnlinkedPortal() {
//         Portal portal = new Portal(0, 0, "P");
//         Exception exception = assertThrows(RuntimeException.class, () ->
//                 portal.interact(new Entity(0, 0), null)
//         );

//         assertEquals("No linked portal for ID: P", exception.getMessage());

//     }

//     @Test
//     public void TestPortalInteractionWithEntity() {
//         Entity entity = new Entity(portal1.getX(), portal1.getY());
//         portal1.interact(entity, null);
//         assertEquals(entity.getX(), portal1.getX());
//         assertEquals(entity.getY(), portal1.getY());

//         entity = new Entity(portal2.getX(), portal2.getY());
//         portal2.interact(entity, null);
//         assertEquals(entity.getX(), portal2.getX());
//         assertEquals(entity.getY(), portal2.getY());
//     }

//     @Test
//     public void TestPortalInteractionWithMoveable() {
//         Moveable moveable = new Moveable(portal1.getX(), portal1.getY(), dungeon);
//         portal1.interact(moveable, null);
//         assertEquals(moveable.getX(), portal2.getX());
//         assertEquals(moveable.getY(), portal2.getY());

//         portal2.interact(moveable, null);
//         assertEquals(moveable.getX(), portal1.getX());
//         assertEquals(moveable.getY(), portal1.getY());
//     }
// }
