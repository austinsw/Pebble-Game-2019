import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class PebbleGameMainTest {
    
    @Test
    public void testLog() {
        PebbleGameMain game = new PebbleGameMain();
        try {
            FileWriter fw = new FileWriter("./player_output_test.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            ArrayList w_ls = new ArrayList();
            w_ls.add(bw);
            Field outputW = game.getClass().getDeclaredField("outputW");
            outputW.setAccessible(true);
            outputW.set(game, w_ls);
            game.log("test", 0);
            bw.close();
            FileReader fr = new FileReader("./player_output_test.txt");
            BufferedReader br = new BufferedReader(fr);
            assertEquals("test", br.readLine());
            br.close();
        } catch (Exception e) {}
    }
    
    @Test
    public void testGameEnded() {
        PebbleGameMain game = new PebbleGameMain();
        //PebbleGameMain.Player p = new PebbleGameMain.Player(bags, 0);
        try {
            Field running = game.getClass().getDeclaredField("ended");
            game.gameEnded(0, true);
            assertEquals(true, running.get(game));
        } catch (Exception e) { }       
    }
    
    
    // ---Following three are test cases for nested player class.----
    @Test
    public void testRandDraw() {
        List<Integer> expResult1 = new ArrayList<>();
        List<Integer> expResult2 = new ArrayList<>();
        expResult1.add(1);
        Bag b2 = new Bag(0);
        b2.putIn(1);
        PebbleGameMain.Player p = new PebbleGameMain.Player(0);
        p.randDraw(b2, true);
        assertEquals(expResult1, p.getHand());
        assertEquals(expResult2, b2.getPebbles());
    }
    
    @Test
    public void testDiscard_in() {
        List<Integer> expResult1 = new ArrayList<>();
        List<Integer> expResult2 = new ArrayList<>();
        expResult1.add(1);
        Bag b2 = new Bag(0);
        b2.putIn(1);
        PebbleGameMain.Player p = new PebbleGameMain.Player(0);
        p.randDraw(b2, true);
        assertEquals(expResult1, p.getHand());
        Bag b3 = new Bag(0);
        p.discard_in(1, b3, true);
        assertEquals(expResult2, p.getHand());
        assertEquals(expResult1, b3.getPebbles());
    }
    
    @Test
    public void testGetHand() {
        Bag b2 = new Bag(0);
        b2.putIn(1);
        PebbleGameMain.Player p = new PebbleGameMain.Player(0);
        p.randDraw(b2, true);
        List<Integer> expResult = new ArrayList<>();
        expResult.add(1);
        assertEquals(expResult, p.getHand());
    }
    // ------------------------------------------------------------
}
