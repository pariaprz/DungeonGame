// package test;

// import javafx.scene.input.KeyCode;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestInstance;
// import unsw.dungeon.*;

// import static org.junit.jupiter.api.Assertions.*;

// public class TestDirection {

//     @Test
//     public void TestDirectionFromKeyCode() {
//         assertEquals(Direction.LEFT, Direction.fromKeyCode(KeyCode.LEFT));
//         assertEquals(Direction.RIGHT, Direction.fromKeyCode(KeyCode.RIGHT));
//         assertEquals(Direction.UP, Direction.fromKeyCode(KeyCode.DOWN));
//         assertEquals(Direction.DOWN, Direction.fromKeyCode(KeyCode.UP));
//     }

//     @Test
//     public void TestKeyCodeFromDirection() {
//         assertEquals(KeyCode.LEFT, Direction.toKeyCode(Direction.LEFT));
//         assertEquals(KeyCode.RIGHT, Direction.toKeyCode(Direction.RIGHT));
//         assertEquals(KeyCode.DOWN, Direction.toKeyCode(Direction.UP));
//         assertEquals(KeyCode.UP, Direction.toKeyCode(Direction.DOWN));
//     }

//     @Test
//     public void TestPositionFromDirection() {
//         assertEquals(Direction.LEFT.fromPosition(new Position(2, 2)), new Position(1, 2));
//         assertEquals(Direction.RIGHT.fromPosition(new Position(2, 2)), new Position(3, 2));
//         assertEquals(Direction.DOWN.fromPosition(new Position(2, 2)), new Position(2, 1));
//         assertEquals(Direction.UP.fromPosition(new Position(2, 2)), new Position(2, 3));
//     }

//     @Test
//     public void TestDirectionFromPositions() {
//         assertEquals(Direction.fromPositions(new Position(2, 2), new Position(1, 2)), Direction.LEFT);
//         assertEquals(Direction.fromPositions(new Position(2, 2), new Position(3, 2)), Direction.RIGHT);
//         assertEquals(Direction.fromPositions(new Position(2, 2), new Position(2, 1)), Direction.DOWN);
//         assertEquals(Direction.fromPositions(new Position(2, 2), new Position(2, 3)), Direction.UP);
//         assertNull(Direction.fromPositions(new Position(2, 2), new Position(2, 2)));
//     }

//     @Test
//     public void InvertDirection() {
//         assertEquals(Direction.LEFT.invert(), Direction.RIGHT);
//         assertEquals(Direction.RIGHT.invert(), Direction.LEFT);
//         assertEquals(Direction.DOWN.invert(), Direction.UP);
//         assertEquals(Direction.UP.invert(), Direction.DOWN);
//     }
// }

