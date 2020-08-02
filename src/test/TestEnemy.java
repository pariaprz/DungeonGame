// package test;

// import javafx.scene.input.KeyCode;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestInstance;
// import unsw.dungeon.*;

// import static org.junit.jupiter.api.Assertions.*;

// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
// public class TestEnemy {

//     private Dungeon dungeon;
//     private Enemy enemy;
//     @BeforeEach
//     void BeforeEach() {
//         dungeon = new Dungeon(10, 10);
//         enemy = new Enemy(2, 1, dungeon);
//     }

//     @Test
//     void TestEntitiesInteractingWithPlayer() {
//         assertTrue(enemy.canEntityMoveHere(new Enemy(0, 2, dungeon)));
//         assertTrue(enemy.canEntityMoveHere(new Enemy(2, 2, dungeon)));
//         assertTrue(enemy.canEntityMoveHere(new Player(2, 1, dungeon)));
//         assertTrue(enemy.canEntityMoveHere(new Player(2, 3, dungeon)));

//         Player player = new Player(1, 1, dungeon);
//         enemy.interact(player, KeyCode.LEFT);
//         assertTrue(player.isDeleted());
//     }

//     @Test
//     void TestNextMovement() {
//         Player player = new Player(0, 1, dungeon);
//         dungeon.addEntity(player);

//         assertEquals(new Position(2, 1), enemy.getPosition());

//         enemy.move();
//         assertEquals(new Position(1, 1), enemy.getPosition());
//         assertFalse(player.isDeleted());

//         player.moveToPosition(new Position(3, 1));
//         enemy.move();
//         assertEquals(new Position(2, 1), enemy.getPosition());
//         assertFalse(player.isDeleted());

//         player.moveToPosition(new Position(2, 3));
//         enemy.move();
//         assertEquals(new Position(2, 2), enemy.getPosition());
//         assertFalse(player.isDeleted());

//         player.moveToPosition(new Position(2, 1));
//         enemy.move();
//         assertEquals(new Position(2, 1), enemy.getPosition());
//         assertTrue(player.isDeleted());
//     }

//     @Test
//     void TestNextMovementWhenInvincble() {
//         Player player = new Player(0, 1, dungeon);
//         player.setState(new InvinciblePlayerState(player));
//         dungeon.addEntity(player);

//         assertEquals(new Position(2, 1), enemy.getPosition());

//         enemy.move();
//         assertEquals(new Position(3, 1), enemy.getPosition());
//         assertFalse(player.isDeleted());

//         player.moveToPosition(new Position(4, 1));
//         enemy.move();
//         assertEquals(new Position(2, 1), enemy.getPosition());
//         assertFalse(player.isDeleted());

//         player.moveToPosition(new Position(2, 0));
//         enemy.move();
//         assertEquals(new Position(2, 2), enemy.getPosition());
//         assertFalse(player.isDeleted());

//         player.moveToPosition(new Position(2, 3));
//         enemy.move();
//         assertEquals(new Position(2, 1), enemy.getPosition());
//         assertFalse(player.isDeleted());
//     }
// //
// //    @Test
// //    void TestInvincibleState() {
// //        player.setState(new InvinciblePlayerState(player));
// //        assertFalse(player.attractEnemies());
// //        assertFalse(player.isDeleted());
// //
// //        player.interact(new Boulder(1, 1, dungeon), KeyCode.LEFT);
// //        assertFalse(player.isDeleted());
// //
// //        assertFalse(dungeon.getEntities().get(0).isDeleted());
// //
// //        player.interact(dungeon.getEntities().get(0), KeyCode.LEFT);
// //        assertFalse(player.isDeleted());
// //        assertTrue((dungeon.getEntities().get(0).isDeleted()));
// //        assertEquals(InvinciblePlayerState.STATE_NAME, player.getPlayerState().getStateName());
// //
// //        player.getPlayerState().expireState();
// //        assertEquals(DefaultPlayerState.STATE_NAME, player.getPlayerState().getStateName());
// //    }
// //
// //    @Test
// //    void TestArmedPlayer() {
// //        player.setState(new InvinciblePlayerState(player));
// //
// //        assertFalse(player.hasSword());
// //        assertEquals(0, player.getSwordCount());
// //
// //        player.swingSword();
// //        assertFalse(dungeon.getEntities().get(0).isDeleted());
// //        assertFalse(player.isDeleted());
// //
// //        player.armSword();
// //        assertTrue(player.hasSword());
// //        assertEquals(Player.NUM_SWORD_SWINGS, player.getSwordCount());
// //
// //        player.swingSword();
// //        assertTrue(dungeon.getEntities().get(0).isDeleted());
// //        assertEquals(Player.NUM_SWORD_SWINGS-1, player.getSwordCount());
// //        for (int i = Player.NUM_SWORD_SWINGS-2; i > 0; i--) {
// //            player.swingSword();
// //            assertTrue(player.hasSword());
// //            assertEquals(i, player.getSwordCount());
// //        }
// //        player.swingSword();
// //        assertFalse(player.hasSword());
// //    }
// }
