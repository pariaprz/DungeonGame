package unsw.dungeon;

public class Wall extends Entity {

    public Wall(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }

    //In most cases, player can not move
    //Only when player is in wallwalker state, then the player can walk through walls
    //The borders of game cannot be 
    @Override
    public boolean canEntityMoveHere(Entity entity) {
        if (entity instanceof Player){
            if (((Player) entity).getPlayerStateName().equals("Wallwalker")){    
                if(getX() == 0 || getY() == 0 || getX() == getDungeonWidth()-1 || getY() == getDungeonHeight()-1){
                    return false;
                }
                return true;
            }    
        }
        return false;
    }    
}
