import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class BagTest {
    
    @Test
    public void testGetName() {
        Bag b = new Bag(0);
        assertEquals("bag X", b.getName());
    }

    @Test
    public void testGetPebbles() {
        Bag b = new Bag(0);
        b.putIn(1);
        List<Integer> expResult = new ArrayList<Integer>();
        expResult.add(1);
        assertEquals(expResult, b.getPebbles());
    }

    @Test
    public void testGetSize() {
        Bag b = new Bag(0);
        b.putIn(1);
        assertEquals(1, b.getSize());
    }

    @Test
    public void testInitPebbles() {
        String str = "1 2 3 4 5 6 7 8 9 10";
        Bag b = new Bag(0);
        List<Integer> expResult = new ArrayList<Integer>();
        for (int i = 1; i < 11; i++) {
            expResult.add(i);
        }
        try {
            b.initPebbles(str);
            assertEquals(expResult, b.getPebbles());
        } catch (NumberFormatException nfe) { }
    }

    @Test
    public void testTaken() {
        Bag b = new Bag(0);
        b.putIn(0);
        b.taken(0);
        assertEquals(0, b.getSize());
    }

    @Test
    public void testPutIn() {
        Bag b = new Bag(0);
        b.putIn(0);
        List<Integer> expResult = new ArrayList<Integer>();
        expResult.add(0);
        assertEquals(expResult, b.getPebbles());
    }
    
}
