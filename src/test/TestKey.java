package test;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import unsw.dungeon.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestKey {
    private Dungeon dungeon;
    private Key key;

    private static String KEY_ID = "A";
    @BeforeAll
    public void BeforeEach() {
        dungeon = new Dungeon(10, 10);
        key = new Key(1, 2, KEY_ID);
    }

    @Test
    public void TestEntitiesMovingOnKey() {
        Player player = new Player(1, 1, dungeon);
        assertTrue(key.canEntityMoveHere(player));
        assertTrue(key.canEntityMoveHere(new Entity(1, 2)));
        assertTrue(key.canEntityMoveHere(new Entity(2, 2)));
    }

    @Test
    public void TestEntitiesInteractingWithKey() {
        key.interact(new Entity(1, 2), KeyCode.LEFT);
        assertFalse(key.isDeleted());
        Enemy enemy = new Enemy(2, 2, dungeon);
        key.interact(enemy, KeyCode.RIGHT);
        assertFalse(key.isDeleted());
        Player player = new Player(1, 1, dungeon);
        player.setKey("B");
        key.interact(player, KeyCode.LEFT);
        assertFalse(key.isDeleted());
        player.dropKey();
        key.interact(player, KeyCode.LEFT);
        assertTrue(key.isDeleted());
    }
}

