import java.io.*;
import java.util.*;

/**
 * Player objects represent players in the game: they select their move based on the Board they are given and which side of the board they are playing. They may quit the game before a result is decided. Results are decided by the {@link Game}.
 *
 * You need to provide two implementations of this interface, one called HumanPlayer, which acts under control of the user, and one called ComputerPlayer which plays automatically and as well as it can within the time constraints.
 *
 * Input from the human players and output to the players can be redirected with in and out. Your Player should always use in and out for input and output rather than System.in and System.out.
 * 
 * A default contructor with no parameters is required for each imeplementation of the interface. By default the input should be set to System.in and the output stream to System.out
 *
 * @author Steven Bradley
 * @version 1.0
 */

public class ComputerPlayer implements Player {
    private String name;
    private HashMap<Integer, Integer> movePriority; // Key -> house number, Values -> potentially captured seeds
    private boolean firstMove;
    private InputStream in;
    private OutputStream out;
    private Board cloneBoard;
    public ComputerPlayer()
    {
        this.firstMove = true;
        this.movePriority = new HashMap<Integer, Integer>()
        {{
            put(1,0);
            put(2,0);
            put(3,0);
            put(4,0);
            put(5,0);
            put(6,0);
        }};
    }
    /**
     *
     * Computer player moves should take less than one second to complete the getMove method or they forfeit the game.
     *
     * @param b An copy of the game that the player may experiment with. It should be a copy of the game so that the computer cannot cheat and experiments do not affect game play.
     *
     * @param playerNum
     * the number of the player: 1 or 2.
     *
     * @return the position of the house selected (counting anti-clockwise): a value in the range 1..6
     *
     * @throws QuitGameException if, instead of choosing a house, a human player chooses to quit by entering 'QUIT'. If a computer player throws QuitGameException they forfeit the game ({@link #isComputer()}).
     *
     **/
    public int getMove(Board b, int playerNum) throws QuitGameException
    {
        int seeds = 0;
        if(this.firstMove) 
        {
            this.firstMove = false;
            return 6;
        }
        for(int i = 1; i < 7; i++)
        {
            try
            {
                this.cloneBoard = b.clone();
                cloneBoard.makeMove(i,playerNum);
                this.movePriority.put(i,cloneBoard.getScore(playerNum));
            }
            catch(InvalidHouseException ex)
            {
                this.movePriority.put(i,-1);
            }
            catch(InvalidMoveException ex)
            {
                this.movePriority.put(i,-1);
            }
        }
        this.sortByValue();
        HashMap.Entry<Integer, Integer> entry = this.movePriority.entrySet().iterator().next();
        //seeds = entry.getValue();
        //if(seeds != 0) 
        int move = entry.getKey();
        this.movePriority = new HashMap<Integer, Integer>()
        {{
            put(1,0);
            put(2,0);
            put(3,0);
            put(4,0);
            put(5,0);
            put(6,0);
        }};
        return move;
        //else
    }
    
    /**
     * returns true is this is a computer player. Computer players are limited to one second per move (on E216 computers) and forfeit the game if they quit or make an invalid move.
     **/
    public boolean isComputer()
    {
        return true;
    }

    
    /**
     * set the input stream for human commands (house numbers and QUIT).
     *
     **/
    public void setIn(InputStream in)
    {
        this.in = in;
    }
    
    /**
     * set the output stream for board state 
     */
    public void setOut(PrintStream out)
    {
        this.out = out;
    }
        
    // Taken from: http://www.programcreek.com/2013/03/java-sort-map-by-value/
    public void sortByValue() {	 
    	List list = new LinkedList(this.movePriority.entrySet());
     
    	Collections.sort(list, new Comparator() {
    		public int compare(Object o1, Object o2) {
    			return -((Comparable) ((Map.Entry) (o1)).getValue())
    						.compareTo(((Map.Entry) (o2)).getValue());
    		}
    	});
     
    	Map sortedMap = new LinkedHashMap();
    	for (Iterator it = list.iterator(); it.hasNext();) {
    		Map.Entry entry = (Map.Entry) it.next();
    		sortedMap.put(entry.getKey(), entry.getValue());
    	}
    	this.movePriority = (HashMap) sortedMap;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
}


