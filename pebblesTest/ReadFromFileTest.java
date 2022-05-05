import org.junit.Test;
import static org.junit.Assert.*;

public class ReadFromFileTest {
    
    @Test
    public void testReadFileAsString(){
        String fileName = "test.txt";
        String expResult = "1 2 3 4 5 6 7 8 9 10";
        try {
            String result = ReadFromFile.readFileAsString(fileName);
            assertEquals(expResult, result);
        } catch (Exception e) { }
    }
}
