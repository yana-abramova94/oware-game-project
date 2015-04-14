import java.util.ArrayList;
/**
 *
 * @author Yana Abramova
 * @version 1.0
 *
 */
public class BoardClass implements Board
{
    private int[] houses;
    private int scoreHouse1;
    private int scoreHouse2;    
    
    public BoardClass()
    {
        this.houses = new int[]{ 4,4,4,4,4,4,4,4,4,4,4,4 };
        this.scoreHouse1 = 0;
        this.scoreHouse2 = 0;
    }
     
    public void makeMove(int house, int playerNum) throws InvalidHouseException, InvalidMoveException
    {
        boolean opponentEmpty = true;
        for(int i = 1; i < 7; i++)
        {
            if(this.getSeeds(i,3-playerNum) != 0) opponentEmpty = false;
        }
        boolean existMove = false;
        if(opponentEmpty == true)
        {
            for(int i = 1; i < 7; i++)
            {
                if(this.getSeeds(i,playerNum) + i > 6) existMove = true;
            }
        }
        else existMove = true;
        
        Board cloneBoard = this.clone();
        if(existMove)
        {
            int sowing = this.getSeeds(house, playerNum);
            if (sowing == 0) throw new InvalidMoveException("Invalid move: the selected house is empty");
            cloneBoard.setSeeds(0, house, playerNum);
            int currentHouse = 0;
            for(int i = 1; sowing > 0; sowing--, i++)
            {
                currentHouse = (house - 1  + 6*(playerNum-1)+ i)%12;
                if(currentHouse == (house - 1  + 6*(playerNum-1))) 
                {
                    sowing++;
                    continue;
                }
                if((house - 1 + 6*(playerNum-1) + i)%12 > -1 && (house - 1 + 6*(playerNum-1) + i)%12 < 6)
                {
                    cloneBoard.sowSeed(currentHouse+1, 1);
                }
                else
                {
                    cloneBoard.sowSeed(currentHouse-5, 2);
                }
            }
            
            int minHouse = playerNum == 1 ? 6 : 0;
            int maxHouse = playerNum == 1 ? 11 : 5;
            int captured = 0;
            for(int i = currentHouse; i >= minHouse && i <= maxHouse; i--)
            {
                int seeds = cloneBoard.getSeeds(i%6+1, 3-playerNum);
                if(seeds != 2 && seeds != 3) break;
                cloneBoard.setSeeds(0, i%6+1, 3-playerNum);
                captured += seeds;
            }
            cloneBoard.addScore(captured, playerNum);
            
            boolean valid = false;
            
            for(int i = 1; i < 7; i++)
            {
                if(cloneBoard.getSeeds(i,3-playerNum) != 0) valid = true;
            }
           
            if(valid == false) throw new InvalidMoveException("Invalid move: the other player would not have any seeds to play his turn");
        }
        else
        {
            int captured = 0;
            for(int i = 1; i < 7; i++)
            {
                int seeds = cloneBoard.getSeeds(i, playerNum);
                cloneBoard.setSeeds(0, i, playerNum);
                captured += seeds;
            }
            cloneBoard.addScore(captured, playerNum);
        }
        
        for(int i = 1; i < 7; i++)
        {
            this.setSeeds(cloneBoard.getSeeds(i,1),i,1);
        }
        for(int i = 1; i < 7; i++)
        {
            this.setSeeds(cloneBoard.getSeeds(i,2),i,2);
        }
        this.setScore(cloneBoard.getScore(1),1);
        this.setScore(cloneBoard.getScore(2),2);
    }

    public int getSeeds(int house, int playerNum) throws InvalidHouseException
    {
        if(house < 1 || house > 6 || playerNum < 1 || playerNum > 2) throw new InvalidHouseException("Invalid house number.");
        int seeds = this.houses[(playerNum-1)*6 + house-1];         
        return seeds;
    }

    public void sowSeed(int house, int playerNum) throws InvalidHouseException
    {
        if(playerNum != 1 && playerNum != 2)
        {
            throw new InvalidHouseException("Invalid player number: choose between 1 and 2.");
        }
        if(house < 1 || house > 6) 
        {
            throw new InvalidHouseException("Invalid house number: choose within 1 and 6.");
        }
        this.houses[(playerNum-1)*6 + house-1]++;
    }

    public void setSeeds(int seeds, int house, int playerNum) throws InvalidHouseException
    {
        if(playerNum != 1 && playerNum != 2)
        {
            throw new InvalidHouseException("Invalid player number: choose between 1 and 2.");
        }
        if(house < 1 || house > 6) 
        {
            throw new InvalidHouseException("Invalid house number: choose within 1 and 6.");
        }
        this.houses[(playerNum - 1)*6 + house-1] = seeds;
    }

    public int getScore(int playerNum) throws IllegalStateException
    {
        if(playerNum != 1 && playerNum != 2)
        {
            throw new IllegalStateException("Invalid player number: choose between 1 and 2.");
        }
        if(playerNum == 1)
        {
            return this.scoreHouse1;
        }
        return this.scoreHouse2;
    }

    public void addScore(int seeds, int playerNum) throws IllegalStateException
    {
        if(playerNum != 1 && playerNum != 2)
        {
            throw new IllegalStateException("Invalid player number: choose between 1 and 2.");
        }
        if(seeds < 0) throw new IllegalStateException("The number of seeds is negative");
        if(playerNum == 1)
        {
            this.scoreHouse1 += seeds;
        }
        else
        {
            this.scoreHouse2 += seeds;
        }
    }

    public void setScore(int seeds, int playerNum) throws IllegalStateException
    {
        if(playerNum != 1 && playerNum != 2)
        {
            throw new IllegalStateException("Invalid player number: choose between 1 and 2.");
        }
        if(seeds < 0) throw new IllegalStateException("The number of seeds is negative");
        if(playerNum == 1)
        {
            this.scoreHouse1 = seeds;
        }
        this.scoreHouse2 = seeds;
    }

    public String toString()
    {
        String state = new String();
        state += "Board:\n\t\tPlayer 2: Score House = " + this.getScore(2) + "\n";
        state += "\t  (6) " + this.houses[11]
                +" (5) " + this.houses[10]
                +" (4) " + this.houses[9]
                +" (3) " + this.houses[8]
                +" (2) " + this.houses[7]
                +" (1) " + this.houses[6] + "\n"
                +"\t  (1) " + this.houses[0]
                +" (2) " + this.houses[1]
                +" (3) " + this.houses[2]
                +" (4) " + this.houses[3]
                +" (5) " + this.houses[4]
                +" (6) " + this.houses[5] + "\n"
                +"\t\tPlayer 1: Score House = " + this.getScore(1) + "\n";
       return state;
    }

    public Board clone()
    {
        Board clone = new BoardClass();
        try
        {
            for(int i = 1; i < 7; i++)
            {
                clone.setSeeds(this.getSeeds(i,1),i,1);
            }
            for(int i = 1; i < 7; i++)
            {
                clone.setSeeds(this.getSeeds(i,2),i,2);
            }
        }
        catch(InvalidHouseException ex)
        {
            System.out.println(ex.getMessage());
        }
        clone.setScore(this.getScore(1),1);
        clone.setScore(this.getScore(2),2);
        return clone;
    }

    public boolean equals(Object o) throws IllegalStateException
    {   
        if(o == null) throw new IllegalStateException("The argument Object o is null");
        Board other = (Board) o;
        return this.toString().equals(other.toString());
    }
}
