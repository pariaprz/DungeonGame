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
 * @author Robert Clifton-Everest
 *
 */
public class Dungeon {

    private int width, height;
    private List<Entity> entities;
    private Goal goal;

    public Dungeon(int width, int height, Goal goal) {
        this.width = width;
        this.height = height;
        this.entities = new ArrayList<>();
        this.goal = goal;
    }

    public Dungeon(int width, int height) {
        this(width, height, null);
    }

    public Goal getGoal() {
        return goal;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Player getPlayer() {
        return (Player) getEntities()
                .stream()
                .filter(entity -> entity instanceof Player)
                .findFirst().orElse(null);
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Entity> getEntitiesAt(Position p) {
        return getEntitiesAt(p.x, p.y);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
        
    }

    public List<Enemy> getEnemies(){
        List<Enemy> enemies = new ArrayList<>();
        for (Entity e: this.entities){
            if (e instanceof Enemy){
                enemies.add((Enemy) e);
            }
        }
        return enemies;
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

    public void linkPortals(Portal p){
        for (Entity e : getEntities()){
            System.out.println("hello there");
            
            
        }
    }
}
