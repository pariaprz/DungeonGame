package test;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import unsw.dungeon.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestGoal {

    private Dungeon dungeon;

    @BeforeEach
    public void BeforeEach() {
        dungeon = new Dungeon(10, 10);
        dungeon.addEntity(new Treasure(5, 4));
        dungeon.addEntity(new Treasure(5, 5));
        dungeon.addEntity(new Enemy(7, 6, dungeon));
        dungeon.addEntity(new Enemy(7, 7, dungeon));
        dungeon.addEntity(new Boulder(4, 3, dungeon));
        dungeon.addEntity(new Boulder(3, 2, dungeon));
        dungeon.addEntity(new Switch(2, 2, dungeon));
        dungeon.addEntity(new Switch(3, 3, dungeon));
        dungeon.addEntity(new Exit(6, 5, dungeon));
        dungeon.addEntity(new Exit(6, 6, dungeon));
        dungeon.addEntity(new Player(0, 0, dungeon));
    }

    @Test
    public void TestExitGoal() {
        Goal goal = new Goal(new ExitGoalEngine());
        assertFalse(goal.computeComplete(dungeon));

        dungeon.getPlayer().setPosition(6, 5);
        assertTrue(goal.computeComplete(dungeon));

        dungeon.getPlayer().setPosition(6, 6);
        assertTrue(goal.computeComplete(dungeon));

        dungeon.getPlayer().setPosition(0, 0);
        assertFalse(goal.computeComplete(dungeon));

        assertEquals(1, goal.getGoalEngine().getSubscriptionTopics().size());
        assertEquals(Exit.class, goal.getGoalEngine().getSubscriptionTopics().get(0).getKey());
    }

    @Test
    public void TestTreasureGoal() {
        Goal goal = new Goal(new TreasureGoalEngine());
        assertFalse(goal.computeComplete(dungeon));

        dungeon.removeEntity(dungeon.getEntities().get(0));
        assertFalse(goal.computeComplete(dungeon));

        dungeon.removeEntity(dungeon.getEntities().get(0));
        assertTrue(goal.computeComplete(dungeon));

        assertEquals(1, goal.getGoalEngine().getSubscriptionTopics().size());
        assertEquals(Treasure.class, goal.getGoalEngine().getSubscriptionTopics().get(0).getKey());
    }

    @Test
    public void TestEnemyGoal() {
        Goal goal = new Goal(new EnemyGoalEngine());
        assertFalse(goal.computeComplete(dungeon));

        dungeon.removeEntity(dungeon.getEntities().get(2));
        assertFalse(goal.computeComplete(dungeon));

        dungeon.removeEntity(dungeon.getEntities().get(2));
        assertTrue(goal.computeComplete(dungeon));

        assertEquals(1, goal.getGoalEngine().getSubscriptionTopics().size());
        assertEquals(Enemy.class, goal.getGoalEngine().getSubscriptionTopics().get(0).getKey());
    }

    @Test
    public void TestSwitchGoal() {
        Goal goal = new Goal(new SwitchesGoalEngine());
        assertFalse(goal.computeComplete(dungeon));

        dungeon.getEntities().get(4).setPosition(2, 2);
        assertFalse(goal.computeComplete(dungeon));

        dungeon.getEntities().get(5).setPosition(3, 3);
        assertTrue(goal.computeComplete(dungeon));

        dungeon.removeEntity(dungeon.getEntities().get(6));
        assertTrue(goal.computeComplete(dungeon));

        dungeon.removeEntity(dungeon.getEntities().get(6));
        assertTrue(goal.computeComplete(dungeon));

        assertEquals(1, goal.getGoalEngine().getSubscriptionTopics().size());
        assertEquals(Boulder.class, goal.getGoalEngine().getSubscriptionTopics().get(0).getKey());
    }

    @Test
    public void TestANDGoal() {
        ComplexGoalEngine goalEngine = new ANDGoalEngine();
        goalEngine.addGoal(new Goal(new TreasureGoalEngine()));
        goalEngine.addGoal(new Goal(new ExitGoalEngine()));
        Goal goal = new Goal(goalEngine);

        assertFalse(goal.computeComplete(dungeon));

        dungeon.getPlayer().setPosition(6, 5);
        assertFalse(goal.computeComplete(dungeon));

        dungeon.getPlayer().setPosition(0, 0);

        dungeon.removeEntity(dungeon.getEntities().get(0));
        assertFalse(goal.computeComplete(dungeon));
        dungeon.removeEntity(dungeon.getEntities().get(0));
        assertFalse(goal.computeComplete(dungeon));

        dungeon.getPlayer().setPosition(6, 5);
        assertTrue(goal.computeComplete(dungeon));

        assertEquals(2, goal.getGoalEngine().getSubscriptionTopics().size());
    }

    @Test
    public void TestORGoal() {
        ComplexGoalEngine goalEngine = new ORGoalEngine();
        goalEngine.addGoal(new Goal(new TreasureGoalEngine()));
        goalEngine.addGoal(new Goal(new ExitGoalEngine()));
        Goal goal = new Goal(goalEngine);

        assertFalse(goal.computeComplete(dungeon));

        dungeon.getPlayer().setPosition(6, 5);
        assertTrue(goal.computeComplete(dungeon));

        dungeon.getPlayer().setPosition(0, 0);

        dungeon.removeEntity(dungeon.getEntities().get(0));
        assertFalse(goal.computeComplete(dungeon));
        dungeon.removeEntity(dungeon.getEntities().get(0));
        assertTrue(goal.computeComplete(dungeon));

        dungeon.getPlayer().setPosition(6, 5);
        assertTrue(goal.computeComplete(dungeon));

        assertEquals(2, goal.getGoalEngine().getSubscriptionTopics().size());
    }

    @Test
    public void TestGoalSubscription() {
        Goal goal = new Goal(new TreasureGoalEngine());
        EntityWrapper treasure = new EntityWrapper(dungeon.getEntities().get(0));

        goal.attachListener(treasure, dungeon);
        assertFalse(goal.isComplete());

        dungeon.removeEntity(dungeon.getEntities().get(0));
        dungeon.removeEntity(dungeon.getEntities().get(0));
        assertFalse(goal.isComplete());

        treasure.setDeleted();
        assertTrue(goal.isComplete());
    }
}