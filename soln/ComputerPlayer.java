import java.io.*;
import java.util.*;

/**
 *
 * @author Yana Abramova
 * @version 1.0
 *
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

    public int getMove(Board b, int playerNum) throws QuitGameException, IllegalStateException
    {
        if(b == null) throw new IllegalStateException("The argument Board b is null");
        if(playerNum != 1 && playerNum != 2) throw new IllegalStateException("The argument int playerNum is different from 1 and 2");
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

    public boolean isComputer()
    {
        return true;
    }

    public void setIn(InputStream in) throws IllegalStateException
    {
        if(in == null) throw new IllegalStateException("The argument InputStream in is null");
        this.in = in;
    }

    public void setOut(PrintStream out) throws IllegalStateException
    {
        if(out == null) throw new IllegalStateException("The argument PrintStream out is null");
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
    
    public void setName(String name) throws IllegalStateException
    {
        if(name == null) throw new IllegalStateException("The argument String name is null");
        this.name = name;
    }
}


