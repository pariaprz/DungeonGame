package test;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import unsw.dungeon.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestDungeonLoader {

    private JSONObject json;

    @BeforeEach
    void BeforeEach() {
        json = new JSONObject();
        json.put("width", 10);
        json.put("height", 10);
        JSONArray entities = new JSONArray();
        entities.put(new JSONObject().put("x", 0).put("y", 0).put("type", "wall"));
        entities.put(new JSONObject().put("x", 2).put("y", 1).put("type", "player"));
        entities.put(new JSONObject().put("x", 2).put("y", 2).put("type", "treasure"));
        entities.put(new JSONObject().put("x", 3).put("y", 2).put("type", "portal").put("id", "A"));
        entities.put(new JSONObject().put("x", 3).put("y", 3).put("type", "portal").put("id", "A"));
        entities.put(new JSONObject().put("x", 2).put("y", 4).put("type", "key").put("id", "B"));
        entities.put(new JSONObject().put("x", 3).put("y", 4).put("type", "door").put("id", "B"));
        entities.put(new JSONObject().put("x", 4).put("y", 2).put("type", "switch"));
        entities.put(new JSONObject().put("x", 4).put("y", 3).put("type", "boulder"));
        entities.put(new JSONObject().put("x", 5).put("y", 5).put("type", "enemy"));
        entities.put(new JSONObject().put("x", 9).put("y", 9).put("type", "exit"));
        entities.put(new JSONObject().put("x", 8).put("y", 9).put("type", "sword"));
        entities.put(new JSONObject().put("x", 9).put("y", 8).put("type", "invincibility"));

        JSONObject goalCondition = new JSONObject();
        JSONArray subgoals = new JSONArray();
        JSONArray subSubgoals = new JSONArray();
        subSubgoals.put(new JSONObject().put("goal", "treasure"));
        subSubgoals.put(new JSONObject().put("goal", "boulders"));
        subgoals.put(new JSONObject().put("goal", "enemies"))
                .put(new JSONObject().put("goal", "exit"))
                .put(new JSONObject().put("goal", "OR").put("subgoals", subSubgoals));

        goalCondition.put("goal", "AND").put("subgoals", subgoals);
        json.put("goal-condition", goalCondition);
        json.put("entities", entities);
    }

    @Test
    void TestPositionOfEntities() {
        DungeonLoader loader = new DungeonLoader(json);
        Dungeon dungeon = loader.load();
        assertEquals(new Position(0, 0), dungeon.getEntities(Wall.class).get(0).getPosition());
        assertEquals(new Position(2, 1), dungeon.getEntities(Player.class).get(0).getPosition());
        assertEquals(new Position(2, 2), dungeon.getEntities(Treasure.class).get(0).getPosition());
        assertEquals(new Position(3, 2), dungeon.getEntities(Portal.class).get(0).getPosition());
        assertEquals(new Position(3, 3), dungeon.getEntities(Portal.class).get(1).getPosition());
        assertEquals(new Position(2, 4), dungeon.getEntities(Key.class).get(0).getPosition());
        assertEquals(new Position(3, 4), dungeon.getEntities(Door.class).get(0).getPosition());
        assertEquals(new Position(4, 2), dungeon.getEntities(Switch.class).get(0).getPosition());
        assertEquals(new Position(4, 3), dungeon.getEntities(Boulder.class).get(0).getPosition());
        assertEquals(new Position(5, 5), dungeon.getEntities(Enemy.class).get(0).getPosition());
        assertEquals(new Position(9, 9), dungeon.getEntities(Exit.class).get(0).getPosition());
        assertEquals(new Position(8, 9), dungeon.getEntities(Sword.class).get(0).getPosition());
        assertEquals(new Position(9, 8), dungeon.getEntities(Invincibility.class).get(0).getPosition());
    }

    @Test
    void TestIdOfEntities() {
        DungeonLoader loader = new DungeonLoader(json);
        Dungeon dungeon = loader.load();
        Portal portal1 = dungeon.getEntities(Portal.class).get(0);
        Portal portal2 = dungeon.getEntities(Portal.class).get(1);
        assertEquals(portal2, portal1.getLinkedPortal());
        assertEquals(portal1, portal2.getLinkedPortal());
        assertEquals("A", portal1.getPortalID());
        assertEquals("A", portal2.getPortalID());


        Door door = dungeon.getEntities(Door.class).get(0);
        Key key = dungeon.getEntities(Key.class).get(0);
        Player player = dungeon.getPlayer();
        key.interact(player, KeyCode.LEFT);

        assertTrue(player.holdsKey());
        int[] count = {0};
        door.status().addListener((ObservableValue<? extends String> o, String oldVal, String newVal) -> {
            assertEquals(Door.OPEN_STATUS, newVal);
            count[0]++;
        });
        door.interact(player, KeyCode.LEFT);
        assertEquals(1, count[0]);
    }

    @Test
    void TestGoalConfiguration() {
        DungeonLoader loader = new DungeonLoader(json);
        Goal goal = loader.loadGoal();
        assertFalse(goal.isComplete());
        assertTrue(goal.getGoalEngine() instanceof ANDGoalEngine);

        ANDGoalEngine andGoalEngine = (ANDGoalEngine) goal.getGoalEngine();
        assertEquals(3, andGoalEngine.getChildGoals().size());
        assertTrue(andGoalEngine.getChildGoals().get(0).getGoalEngine() instanceof EnemyGoalEngine);
        assertTrue(andGoalEngine.getChildGoals().get(1).getGoalEngine() instanceof ExitGoalEngine);
        assertTrue(andGoalEngine.getChildGoals().get(2).getGoalEngine() instanceof ORGoalEngine);

        ORGoalEngine orGoalEngine = (ORGoalEngine) andGoalEngine.getChildGoals().get(2).getGoalEngine();
        assertEquals(2, orGoalEngine.getChildGoals().size());
        assertTrue(orGoalEngine.getChildGoals().get(0).getGoalEngine() instanceof TreasureGoalEngine);
        assertTrue(orGoalEngine.getChildGoals().get(1).getGoalEngine() instanceof SwitchesGoalEngine);
    }
}
