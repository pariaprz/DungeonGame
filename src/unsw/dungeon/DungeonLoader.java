package unsw.dungeon;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Loads a dungeon from a .json file.
 *
 * By extending this class, a subclass can hook into entity creation. This is
 * useful for creating UI elements with corresponding entities.
 *
 * @author Robert Clifton-Everest
 *
 */
public class DungeonLoader {

    private JSONObject json;
    public DungeonLoader(String filename) throws FileNotFoundException {
        json = new JSONObject(new JSONTokener(new FileReader("dungeons/" + filename)));
    }

    /**
     * Parses the JSON to create a dungeon.
     * @return
     */
    public Dungeon load() {
        int width = json.getInt("width");
        int height = json.getInt("height");

        JSONObject goalCondition = json.getJSONObject("goal-condition");
        String goalTopic = goalCondition.getString("goal");
        JSONArray subgoals = goalCondition.has("subgoals") ?
                goalCondition.getJSONArray("subgoals") : null;
        Goal goal = getGoal(goalTopic, subgoals);

        Dungeon dungeon = new Dungeon(width, height, goal);

        JSONArray jsonEntities = json.getJSONArray("entities");

        for (int i = 0; i < jsonEntities.length(); i++) {
            loadEntity(dungeon, jsonEntities.getJSONObject(i));
        }
        return dungeon;
    }

    private void loadEntity(Dungeon dungeon, JSONObject json) {
        String type = json.getString("type");
        int x = json.getInt("x");
        int y = json.getInt("y");

        // TODO: Uncomment this.
//        validatePosition(dungeon, new Position(x, y));

        Entity entity = null;
        switch (type) {
        case "player":
            Player player = new Player(x, y, dungeon);
//            dungeon.setPlayer(player);
            entity = player;
            break;
        case "wall":
            entity = new Wall(x, y);
            break;
        case "switch":
            entity = new Switch(x, y);
            break;
        case "boulder":
            entity = new Boulder(x, y, dungeon);
            break;
        case "exit":
            entity = new Exit(x, y, dungeon);
            break;
        case "sword":
            entity = new Sword(x, y);
            break;
        case "invincibility":
            entity = new Invincibility(x, y);
            break;
        case "door":
            String doorId = json.getString("id");
            if (doorId == null) {
                throw new RuntimeException("No ID provided for door");
            }
            entity = new Door(x, y, doorId);
            break;
        case "key":
            String keyId = json.getString("id");
            if (keyId == null) {
                throw new RuntimeException("No ID provided for key");
            }
            entity = new Key(x, y, keyId);
            break;
        case "portal":
            String portalId = json.getString("id");
            if (portalId == null) {
                throw new RuntimeException("No ID provided for portal");
            }
            entity = new Door(x, y, portalId); // TODO: Add portal with same id as linked portal.
            break;
        case "enemy":
            entity = new Enemy(x, y, dungeon);
            break;
        case "treasure":
            entity = new Treasure(x, y);
            break;
        }
        if (entity != null) {
            dungeon.addEntity(entity);
        } else {
            System.out.println("TYPE not handled: " + type);
        }
        //TODO CHANGE BACK WHEN FINISHED ADDING OTHER ENTITIES
        //dungeon.addEntity(entity);
    }

    private Goal getGoal(String goalTopic, JSONArray subgoals) {
        switch (goalTopic) {
            case "exit":
                return new Goal(new ExitGoalEngine());
            case "enemies":
                return new Goal(new EnemyGoalEngine());
            case "boulders":
                return new Goal(new SwitchesGoalEngine());
            case "treasure":
                return new Goal(new TreasureGoalEngine());
            case "AND":
            case "OR":
                List<Goal> children = new ArrayList<>();
                for (int i = 0; i < subgoals.length(); i++) {
                    String childTopic = subgoals.getJSONObject(i).getString("goal");
                    JSONArray subSubgoals = subgoals.getJSONObject(i).has("subgoals") ?
                            subgoals.getJSONObject(i).getJSONArray("subgoals") : null;
                    children.add(getGoal(childTopic, subSubgoals));
                }
                if (goalTopic.equals("AND")) return new Goal(new ANDGoalEngine(children));
                else return new Goal(new ORGoalEngine(children));
            default:
                System.out.println("Unsupported goal: " + goalTopic);
        }
        return null;
    }

    private void validatePosition(Dungeon dungeon, Position position) {
        List<Entity> others = dungeon.getEntitiesAt(position);
        if (position.x >= dungeon.getWidth() || position.x < 0 || position.y >= dungeon.getHeight() || position.y < 0) {
            throw new RuntimeException("Invalid position for entity (" + position.x + ", " + position.y + ")");
        }
        if (!others.isEmpty()) {
            throw new RuntimeException("Multiple entities placed at (" + position.x + ", " + position.y + ")");
        }
    }

    // TODO Create additional abstract methods for the other entities

}
