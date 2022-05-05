import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class PebbleGameMain {
    static PebbleGameMain pebbleGame;
    static BufferedWriter bw = null;
    static FileWriter fw = null;
    
    private static int numOfPlayers = 0;
    private static boolean ended = false; 
    private static ArrayList<Bag> bags = new ArrayList<Bag>(6);
    private static ArrayList<Player> players = new ArrayList<Player>(numOfPlayers);
    private static ArrayList<Integer> chosenBagNum = new ArrayList<>(numOfPlayers);
    private static ReadFromFile readFile = new ReadFromFile();
    private static ArrayList<BufferedWriter> outputWriters = new ArrayList<BufferedWriter>();
    
    class WeightException extends Exception {}
    class FileFormattingException extends Exception {}
    class PlayerNumException extends Exception {}
    
    public static void main(String[] args) throws Exception {
        pebbleGame = new PebbleGameMain();  // allows threads to reference methods in main thread
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < 6; i++) {
            Bag b = new Bag(i); // Creating bag XYZABC & add to ArrayList
            bags.add(b);
        }
        
        System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
        System.out.println("     WELCOME TO \n  THE PEBBLE GAME");
        System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\n(Enter 'E' to exit.)\n");
        System.out.println("Please select the number of players (2-4): ");       
        while (true) {
            try {   // Check for invalid numOfPlayers entry.
                numOfPlayers = sc.nextInt();         
            } catch (InputMismatchException e){
                checkE(sc.nextLine());
            }
            if(numOfPlayers >= 2 && numOfPlayers <= 4){
                System.out.println("Number of players chosen: " + numOfPlayers);
                break;
            } else {
                System.out.println("Input must be an integer between 2 and 4.");
            }
        }
        
        String fileName = "";
        String str = "";   
        for (int i = 0; i < 3; i++) {
            System.out.println("\nPlease enter location of bag number " + i + " to load: \n ** e.g /Users/username/Desktop/test.csv **");
            while (true) {
                try {
                    fileName = sc.next();
                    str = readFile.readFileAsString(fileName);  // Read the file, convert into string
                } catch (NoSuchFileException e){    // Check if file exists
                    checkE(fileName);
                }
                try {   // Check for invalid file format.
                    String format = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if(!(format.equals("txt") || format.equals("csv"))) {
                        System.out.println("\nPlease enter a valid location for bag number " + i + ": \n ** e.g /Users/username/Desktop/test.csv **");
                        throw pebbleGame.new FileFormattingException();
                    }
                } catch (FileFormattingException ffe) {
                    continue;
                }
                try {
                    bags.get(i).initPebbles(str);   // Initialize. Put pebbles into the bags
                } catch (NumberFormatException e2) {
                    System.out.println("Illegal file formatting detected! Please select another file.");
                    continue;
                }
                try {   // Check for invalid weights.
                    for(int k : bags.get(i).getPebbles()) {
                        if (k <= 0) {
                            System.out.println("Weight of pebble ("+ k +") cannot be non-positive! Please select another file.");
                            throw pebbleGame.new WeightException();
                        }
                    }
                } catch (WeightException we) {
                    continue;
                }
                try {   // Check for insufficient file values.
                    if (bags.get(i).getSize() < 11 * numOfPlayers) {
                        System.out.println(bags.get(i).getName() + " cannot contain less then 11 x number of player! Please select another file.");
                        throw pebbleGame.new PlayerNumException();
                    }
                } catch (PlayerNumException pne) {
                    continue;
                }
                break;
            }
        }
        
        System.out.print("\nPlease enter output file destination: \n ** e.g /Users/username/Desktop/ **\n");
        String dest = sc.next();
        // Setup output file writers
        for (int i = 0; i < numOfPlayers; i++){
            try {
                String outputPath = dest + "player" + (i + 1) + "_output.txt";
                fw = new FileWriter(outputPath);
                bw = new BufferedWriter(fw);
                outputWriters.add(bw);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        for (int i = 0; i < numOfPlayers; i++) {
            Player p = new Player(i);
            players.add(p);
            p.start();
        }
    }
    
    static class Player extends Thread {
        private ArrayList<Integer> hand = new ArrayList<>();
        private Random rand = new Random();
        private int playerID;
        private int chosenBagID;
        private volatile boolean running = true;
        private Scanner sc = new Scanner(System.in);
        
        Player(int p) {
            playerID = p;
        }
        
        public void run() {
            Random rand2 = new Random();
            chosenBagID = rand2.nextInt(3);
            System.out.println("Player " + (playerID + 1) + " has " + bags.get(chosenBagID).getName());
            for (int j = 0; j < 10; j++) randDraw(bags.get(chosenBagID), false);
            String output = "Player " + (playerID + 1) + " hannd is " + hand + "\n\n";
            log(output, playerID);
            System.out.println("Player " + (playerID + 1) + " has hand: " + hand + " and a total of weight: " + findSum());
            if (winCheck()) PebbleGameMain.pebbleGame.gameEnded(playerID, false);
            while (running) { 
                doRunning(this, false);
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        }
        
        synchronized public void randDraw(Bag bag_, boolean test) {
            int selection = bag_.getPebbles().get(rand.nextInt(bag_.getSize()));
            hand.add(selection);
            bag_.taken(selection);
            String output = "Player " + (playerID + 1) + " has drawn a " + selection + " from " + bag_.getName() + "\n";
            if (!test) log(output, playerID);
        }
        
        synchronized public void discard_in(int selection, Bag bag_, boolean test) {
            hand.remove(new Integer(selection));
            bag_.putIn(selection);
            String output = "Player " + (playerID + 1) + " has discarded a " + selection + " to " + bag_.getName() + "\n";
            if (!test) log(output, playerID);
            output = "Player " + (playerID + 1) + " hannd is " + hand + "\n\n";
            if (!test) log(output, playerID);
        }
        
        public ArrayList<Integer> getHand() {
            return hand;
        }
        
        public int findSum() {
            int sum = 0;
            for (Integer i : hand) {
                sum += i;
            }
            return sum;
        }
        
        public boolean winCheck(){
            if (hand.size() != 10) {
                return false;
            }
            return findSum() == 100;            
        }
        
        public void endGame(){
            running = false;
        }
    }
    
    // The main running method, for players & logging
    synchronized public static void doRunning(Player p, boolean test) {
        if (!ended) {
            System.out.println("\nPlayer " + (p.playerID + 1) + " now has hand: " + p.hand + " and a total weight of " + p.findSum());
            Random rand = new Random();
            int discardP = p.hand.get(rand.nextInt(p.hand.size()));
            while (true) {  // Switching paired bags if black is empty.
                if (bags.get(p.chosenBagID).getSize() != 0) break;
                if (bags.get(p.chosenBagID + 3).getSize() != 0) {
                    Collections.swap(bags, p.chosenBagID, p.chosenBagID + 3);
                    break;
                } else while (true) {
                    int newBagID = rand.nextInt(3);
                    if (newBagID != p.chosenBagID) {    // Get a new bagID if both black & white bags are empty.
                        p.chosenBagID = newBagID;
                        break;
                    }
                }
            }
            p.discard_in(discardP, bags.get(p.chosenBagID + 3), test);
            p.randDraw(bags.get(p.chosenBagID), test);
            String output = "Player " + (p.playerID + 1) + " hann is " + p.hand + "\n\n";
            if (!test) log(output, p.playerID);
            System.out.println("Player " + (p.playerID + 1) + " discarded " + discardP);
            System.out.println("Player " + (p.playerID + 1) + " now has hand: " + p.hand + " and a total weight of " + p.findSum());
            System.out.println(bags.get(p.chosenBagID).getName() + " now contains " + bags.get(p.chosenBagID).getPebbles());
            System.out.println(bags.get(p.chosenBagID + 3).getName() + " now contains " + bags.get(p.chosenBagID + 3).getPebbles());
            if (p.winCheck()) PebbleGameMain.pebbleGame.gameEnded(p.playerID, false);
        }
    }
    
    // check if user type E
    private static void checkE(String input) {
        if (input.equals("E")) {
            System.out.println("Program exited.");
            System.exit(0);
        }
    }
    
    // log the players hand at the end of the turn in their output file
    private static void logHands(boolean finished){
        for (int i = 0; i < players.size(); i++){
            Player p = players.get(i);
            String output = "\nHand at the and of the game is " + p.hand + "\n";
            log(output, i);
        }
    }
    
    // log output to relevant player file
    public static void log(String event, int playerID) {
        try {
            outputWriters.get(playerID).write(event);
        } catch (IOException e){
            System.out.println(e);
        }
    }
    
    synchronized public static void gameEnded(int playerID, boolean test) {
        if (!ended){
            ended = true;
            for (int i = 0; i < players.size(); i++){
                if (!test)log("The game has been won \n\n", i);
                Player p = players.get(i);
                p.endGame();
            }
            // log the final results in all the files
            if (!test) logHands(true);
            // log in all the output files which player has won the game
            for (int j = 0; j < numOfPlayers; j++){
                if (j == playerID){
                    String output = "\nPlayer " + (playerID + 1) + " has won the game";
                    if (!test) log(output, j);
                } else {
                    String output = "\nPlayer " + (j + 1) + " was informed that " + "player " + (playerID + 1) + " had won the game";
                    if (!test) log(output, j);
                }
            }
            System.out.println("Player " + (playerID + 1) + " won the game!");
            System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
            System.out.println("      GAMEOVER");
            System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
            try {
                for (int i = 0; i < numOfPlayers; i++){
                    outputWriters.get(i).close();
                }
            } catch (IOException e) { }
        }
    }
}