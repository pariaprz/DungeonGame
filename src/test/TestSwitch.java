package test;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import unsw.dungeon.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestSwitch {
    private Dungeon dungeon;

    @BeforeAll
    public void BeforeEach() {
        dungeon = new Dungeon(10, 10);
    }

    @Test
    public void TestEntitiesMovingOnKey() {
        Switch switchObj = new Switch(1, 2, dungeon);
        Player player = new Player(1, 1, dungeon);
        assertTrue(switchObj.canEntityMoveHere(player));
        assertTrue(switchObj.canEntityMoveHere(new Entity(1, 2)));
        assertTrue(switchObj.canEntityMoveHere(new Entity(2, 2)));
    }

    @Test
    public void TestEntitiesInteractingWithKey() {
        
    }
}

