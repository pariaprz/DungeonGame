package unsw.dungeon;

import java.io.FileNotFoundException;
import java.io.FileReader;
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
public abstract class DungeonLoader {

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

        Dungeon dungeon = new Dungeon(width, height);

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
            dungeon.setPlayer(player);
            entity = player;
            break;
        case "wall":
            entity = new Wall(x, y);
            break;
        case "switch":
            entity = new Switch(x, y, dungeon);
            break;
        case "boulder":
            entity = new Boulder(x, y, dungeon);
            break;
        case "exit":
            entity = new Exit(x, y, dungeon);
            break;
        case "sword":
            entity = new Exit(x, y, dungeon);
            break;
        case "invincibility":
            entity = new Invincibility(x, y, dungeon);
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
            entity = new Key(x, y, keyId, dungeon);
            break;
        case "portal":
            String portalId = json.getString("id");
            if (portalId == null) {
                throw new RuntimeException("No ID provided for portal");
            }
            entity = new Door(x, y, portalId); // TODO: Add portal with same id as linked portal.
            break;
        case "enemy":
            entity = new Enemy(x, y, dungeon); // TODO: Add portal with same id as linked portal.
            break;
        case "treasure":
            entity = new Treasure(x, y, dungeon); // TODO: Add portal with same id as linked portal.
            break;
        }
        if (entity != null) {
            onLoad(entity);
            dungeon.addEntity(entity);
        } else {
            System.out.println("TYPE not handled: " + type);
        }
        //TODO CHANGE BACK WHEN FINISHED ADDING OTHER ENTITIES
        //dungeon.addEntity(entity);
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

    public abstract void onLoad(Entity player);

    // TODO Create additional abstract methods for the other entities

}
