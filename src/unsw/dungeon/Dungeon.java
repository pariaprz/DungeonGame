/**
 *
 */
package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A dungeon in the interactive dungeon player.
 *
 * A dungeon can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 *
 */
public class Dungeon {

    private final int width, height;
    private final List<Entity> entities;
    private DungeonController controller;

    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.entities = new ArrayList<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Player getPlayer() {
        return getEntities(Player.class).stream().findFirst().orElse(null);
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setController(DungeonController controller) {
        this.controller = controller;
    }

    public<T extends Entity> List<T> getEntities(Class<T> objClass) {
        return entities.stream()
                .filter(objClass::isInstance)
                .map(objClass::cast)
                .collect(Collectors.toList());
    }

    public void createRuntimeEntity(Entity entity) {
        this.addEntity(entity);
        controller.addRuntimeEntity(entity);
    }

    public List<Entity> getEntitiesAt(Position p) {
        return getEntitiesAt(p.x, p.y);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public List<Entity> getEntitiesAt(int x, int y) {
        if (x >= width || x < 0 || y < 0 || y >= height) {
            return List.of(new Border(x, y));
        }
        return entities
                .stream()
                .filter(entity -> entity.getX() == x && entity.getY() == y)
                .collect(Collectors.toList());
    }
    
}
