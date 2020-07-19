package test;


import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import unsw.dungeon.*;

import java.beans.PropertyChangeListener;

;import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestDungeonController {

    Dungeon dungeon;

    @BeforeEach
    void BeforeEach() {
        dungeon = new Dungeon(10, 10);
        dungeon.addEntity(new Player(2, 2, dungeon));
        dungeon.addEntity(new Exit(4, 4, dungeon));
        dungeon.addEntity(new Enemy(3, 3, dungeon));
    }

    @Test
    public void TestControllerInitialState() {
        DungeonController controller = new DungeonController(dungeon, new Goal(new ExitGoalEngine()));

        assertEquals(controller.getGoal().getGoalEngine().getClass(), ExitGoalEngine.class);
        assertEquals(controller.getInitialEntities().size(), 3);
        EntityWrapper player = controller.getInitialEntities().get(0);
        assertEquals(player.entityClass, Player.class);
        assertEquals(player.getPosition(), new Position(2, 2));

        EntityWrapper exit = controller.getInitialEntities().get(1);
        assertEquals(exit.entityClass, Exit.class);
        assertEquals(exit.getPosition(), new Position(4, 4));

        EntityWrapper enemy = controller.getInitialEntities().get(2);
        assertEquals(enemy.entityClass, Enemy.class);
        assertEquals(enemy.getPosition(), new Position(3, 3));
    }
    
    @Test
    public void TestControllerPositionListener() {
        DungeonController controller = new DungeonController(dungeon, new Goal(new ExitGoalEngine()));
        EntityWrapper player = controller.getInitialEntities().get(0);
        PropertyChangeListener changeListener = (event) -> {
            Position newPosition = (Position)event.getNewValue();
            assertEquals(newPosition, new Position(2, 8));
        };
        player.addPositionObserver(changeListener);
        dungeon.getPlayer().setPosition(2, 8);
        player.dropAllSubscribers();

        changeListener = (event) -> {
            Position newPosition = (Position)event.getNewValue();
            assertEquals(newPosition, new Position(8, 8));
        };
        player.addPositionObserver(changeListener);
        dungeon.getPlayer().setPosition(8, 8);
        player.dropAllSubscribers();

        final int[] x = {0};
        changeListener = (event) -> {
            Position newPosition = (Position)event.getNewValue();
            assertEquals(new Position(8, 2), newPosition);
            x[0]++;
        };
        player.addPositionObserver(changeListener);
        dungeon.getPlayer().setPosition(-1, 2);
        player.dropAllSubscribers();
        assertEquals(1, x[0]);

        x[0] = 0;
        changeListener = (event) -> {
            Position newPosition = (Position)event.getNewValue();
            assertEquals(new Position(2, 2), newPosition);
            x[0]++;
        };
        player.addPositionObserver(changeListener);
        dungeon.getPlayer().setPosition(2, 10);
        player.dropAllSubscribers();
        assertEquals(1, x[0]);
    }

    @Test
    public void TestControllerDeleteListener() {
        DungeonController controller = new DungeonController(dungeon, new Goal(new ExitGoalEngine()));
        EntityWrapper enemy = controller.getInitialEntities().get(2);

        final int[] numCalls = {0};
        PropertyChangeListener changeListener = (event) -> {
            numCalls[0]++;
        };
        enemy.addDeleteObserver(changeListener);
        dungeon.getEntities().get(2).isDeletedProperty().setValue(false);
        assertEquals(0, numCalls[0]);
        dungeon.getEntities().get(2).delete();
        assertEquals(1, numCalls[0]);
    }

    @Test
    public void TestControllerStatusListener() {
        DungeonController controller = new DungeonController(dungeon, new Goal(new ExitGoalEngine()));
        EntityWrapper exit = controller.getInitialEntities().get(1);

        final int[] numCalls = {0};
        PropertyChangeListener changeListener = (event) -> {
            assertEquals(Exit.EXIT_STATUS, event.getNewValue());
            numCalls[0]++;
        };
        exit.addStatusObserver(changeListener);
        assertEquals(0, numCalls[0]);
        dungeon.getEntities().get(1).setStatus(Exit.EXIT_STATUS);
        assertEquals(1, numCalls[0]);
        exit.unsubscribeState(changeListener);

        numCalls[0] = 0;
        changeListener = (event) -> {
            assertEquals("RANDOM_STRING!", event.getNewValue());
            numCalls[0]++;
        };
        exit.addStatusObserver(changeListener);
        dungeon.getEntities().get(1).setStatus("RANDOM_STRING!");
        assertEquals(1, numCalls[0]);
        exit.unsubscribeState(changeListener);
    }

    @Test
    public void TestControllerGoalListener() {
        DungeonController controller = new DungeonController(dungeon, new Goal(new ExitGoalEngine()));
        Goal goal = controller.getGoal();
        assertFalse(controller.isGameComplete());
        goal.getCompleteProperty().setValue(true);
        assertTrue(controller.isGameComplete());
    }

    @Test
    public void TestControllerKeyPress() {
        DungeonController controller = new DungeonController(dungeon, new Goal(new ExitGoalEngine()));
        controller.handleKeyPress(KeyCode.LEFT);
        assertEquals(dungeon.getPlayer().getPosition(), new Position(1, 2));
        dungeon.getPlayer().armSword();
        controller.handleKeyPress(KeyCode.SPACE);
        assertEquals(4, dungeon.getPlayer().getSwordCount());
    }
}
