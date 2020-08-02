// package test;

// import javafx.beans.value.ChangeListener;
// import javafx.beans.value.ObservableValue;
// import javafx.scene.input.KeyCode;
// import javafx.util.converter.BooleanStringConverter;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestInstance;
// import unsw.dungeon.*;

// import static org.junit.jupiter.api.Assertions.*;

// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
// public class TestExit {
//     private Dungeon dungeon;

//     @BeforeAll
//     public void BeforeEach() {
//         dungeon = new Dungeon(10, 10);
//     }

//     @Test
//     public void TestEntitiesMovingOnExit() {
//         Exit exit = new Exit(1, 2, dungeon);
//         Player player = new Player(1, 1, dungeon);
//         assertTrue(exit.canEntityMoveHere(player));
//         assertTrue(exit.canEntityMoveHere(new Entity(1, 2)));
//         assertTrue(exit.canEntityMoveHere(new Entity(2, 2)));
//     }

//     @Test
//     public void TestEntitiesOnExitInteract() {
//         Exit exit = new Exit(1, 1, dungeon);

//         final int[] x = {0};
//         exit.status().addListener(
//                 (ObservableValue<? extends String> observableValue,
//                  String oldVal, String newVal) -> {
//                     assertEquals(Exit.EXIT_STATUS, newVal);
//                     x[0]++;
//         });

//         exit.interact(new Boulder(1, 1, dungeon), KeyCode.LEFT);
//         assertEquals(0, x[0]);
//         exit.interact(new Enemy(1, 1, dungeon), KeyCode.LEFT);
//         assertEquals(0, x[0]);

//         exit.interact(new Player(1, 1, dungeon), KeyCode.LEFT);
//         assertEquals(1, x[0]);
//     }
// }

