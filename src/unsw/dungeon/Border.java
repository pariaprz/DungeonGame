package unsw.dungeon;

/**
 * This class represents the border of the map.
 * Ensures that no entity can escape the map.
 * Pretty much identical to a wall,
 * but is a different class so wall can change without introducing new bugs
 */
public class Border extends Entity {

    public Border(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean canEntityMoveHere(Entity entity) {
        return false;
    }
}
