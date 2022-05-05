import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Bag {
    private List<Integer> pebbles = new ArrayList<Integer>();
    private String name;
    
    Bag(int bagID) {
        switch (bagID) {
            case 0:
                name = "bag X"; break;
            case 1:
                name = "bag Y"; break;
            case 2:
                name = "bag Z"; break;
            case 3:
                name = "bag A"; break;
            case 4:
                name = "bag B"; break;
            case 5:
                name = "bag C"; break;
        }
    }
    
    public String getName() {
        return name;
    }
    
    public List<Integer> getPebbles() {
        return pebbles;
    }
    
    public int getSize() {
        return pebbles.size();
    }
    
    public void initPebbles(String str) throws NumberFormatException {
        pebbles.clear();
        pebbles = Arrays.stream(str.split("\\s")).map(Integer::parseInt).collect(Collectors.toList());
    }
    
    public void taken(int selection) {
        synchronized(this) { pebbles.remove(new Integer(selection)); }
        try {
            Thread.sleep(400);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
     
    public void putIn(int selection) {
        synchronized(this) { pebbles.add(selection); }
        try {
            Thread.sleep(400);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}