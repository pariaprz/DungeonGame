package test;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import unsw.dungeon.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestPlayer {

    private Dungeon dungeon;
    private Player player;
    @BeforeEach
    void BeforeEach() {
        dungeon = new Dungeon(10, 10);
        dungeon.addEntity(new Enemy(2, 1, dungeon));
        player = new Player(1, 1, dungeon);
    }

    @Test
    void TestEntitiesInteractingWithPlayer() {
        assertTrue(player.canEntityMoveHere(new Enemy(0, 2, dungeon)));
        assertTrue(player.canEntityMoveHere(new Enemy(2, 2, dungeon)));
        assertTrue(player.canEntityMoveHere(new Enemy(2, 1, dungeon)));
        assertTrue(player.canEntityMoveHere(new Enemy(2, 3, dungeon)));
    }

    @Test
    void TestDefaultPlayerState() {
        assertTrue(player.attractEnemies());

        player.interact(new Boulder(1, 1, dungeon), KeyCode.LEFT);
        assertFalse(player.isDeleted());

        player.interact(dungeon.getEntities().get(0), KeyCode.LEFT);
        assertTrue(player.isDeleted());
        assertEquals(DefaultPlayerState.STATE_NAME, player.getPlayerState().getStateName());
    }

    @Test
    void TestInvincibleState() {
        player.setState(new InvinciblePlayerState(player));
        assertFalse(player.attractEnemies());
        assertFalse(player.isDeleted());

        player.interact(new Boulder(1, 1, dungeon), KeyCode.LEFT);
        assertFalse(player.isDeleted());

        assertFalse(dungeon.getEntities().get(0).isDeleted());

        player.interact(dungeon.getEntities().get(0), KeyCode.LEFT);
        assertFalse(player.isDeleted());
        assertTrue((dungeon.getEntities().get(0).isDeleted()));
        assertEquals(InvinciblePlayerState.STATE_NAME, player.getPlayerState().getStateName());

        player.getPlayerState().expireState();
        assertEquals(DefaultPlayerState.STATE_NAME, player.getPlayerState().getStateName());
    }

    @Test
    void TestArmedPlayer() {
        player.setState(new InvinciblePlayerState(player));

        assertFalse(player.hasSword());
        assertEquals(0, player.getSwordCount());

        player.swingSword();
        assertFalse(dungeon.getEntities().get(0).isDeleted());
        assertFalse(player.isDeleted());

        player.armSword();
        assertTrue(player.hasSword());
        assertEquals(Player.NUM_SWORD_SWINGS, player.getSwordCount());

        player.swingSword();
        assertTrue(dungeon.getEntities().get(0).isDeleted());
        assertEquals(Player.NUM_SWORD_SWINGS-1, player.getSwordCount());
        for (int i = Player.NUM_SWORD_SWINGS-2; i > 0; i--) {
            player.swingSword();
            assertTrue(player.hasSword());
            assertEquals(i, player.getSwordCount());
        }
        player.swingSword();
        assertFalse(player.hasSword());
    }
}
