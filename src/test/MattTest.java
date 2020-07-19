package test;

import org.junit.jupiter.api.Test;
import unsw.dungeon.Dungeon;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MattTest {
    @Test
    public void blahTest(){
        assertEquals("a", "a");
    }
    
    @Test
    public void blahTest2(){
        Dungeon d = new Dungeon(1, 2);
        assertEquals(d.getWidth(), 1);
    }
}

