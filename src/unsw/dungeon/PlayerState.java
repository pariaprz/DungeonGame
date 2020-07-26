package unsw.dungeon;

public interface PlayerState extends State {
    boolean attractsEnemies();
    void interactWithEnemy(Enemy enemy);
    void expireState();
    
}