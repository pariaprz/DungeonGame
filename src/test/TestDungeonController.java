package test;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import unsw.dungeon.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestDungeonController {

    Dungeon dungeon;

    @BeforeEach
    void BeforEach() {
        dungeon = new Dungeon(10, 10);
        dungeon.addEntity(new Player(2, 2, dungeon));
        dungeon.addEntity(new Exit(4, 4, dungeon));
        dungeon.addEntity(new Enemy(3, 3, dungeon));
    }

    @Test
    public void TestEntitiesMovingOnPortalFromAllDirections() {
        System.out.println(dungeon);
        DungeonController controller = new DungeonController(dungeon, new Goal(new TreasureGoalEngine()));
    }
}
