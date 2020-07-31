package unsw.dungeon;

public interface PlayerState extends State {
    boolean attractsEnemies();
    void interactWithEnemy(Slayable enemy);
    void expireState();
    
}