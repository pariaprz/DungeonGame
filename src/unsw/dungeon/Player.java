package unsw.dungeon;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Player extends Entity {

    private Dungeon dungeon;

    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Player(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.dungeon = dungeon;
    }



    public void moveUp() {
        if (noObstacles(getY()-1, getX())){
            if (getY() > 0)
                y().set(getY() - 1);
        }
    }
    
    public void moveDown() {
        if (noObstacles(getY()+1, getX())){
            if (getY() < dungeon.getHeight() - 1)
                y().set(getY() + 1);
            }
        }

    public void moveLeft() {
        if (noObstacles(getY(), getX()-1)){
            if (getX() > 0)
                x().set(getX() - 1);
            }
    }

    public void moveRight() {
        if (noObstacles(getY(), getX()+1)){
            if (getX() < dungeon.getWidth() - 1)
                x().set(getX() + 1);
            }
    }

    //Goes through and finds out if any obstacles (Wall/Boulder/Closed Door) are above the players coordinates
    public boolean noObstacles(int y, int x){
        for (Entity e : dungeon.getEntities()){
            if(e.toString().equals("wall")){
                if(e.getY() == y && e.getX() == x){
                    return false;
                }
            }
        }
        return true;
    }
}
