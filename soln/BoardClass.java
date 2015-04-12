import java.util.ArrayList;
/**
 * Write a description of class BoardClass here.
 * 
 * @author Yana Abramova 
 * @version 1.0
 */
public class BoardClass implements Board
{
    private int[] houses;
    private int scoreHouse1;
    private int scoreHouse2;    
    /**
     * update the board to make a move from the specified house of the specified player
     *
     * @param house in range 1..6 representing the house position (starting from anticlockwise)
     *
     * @param playerNum in range 1..2
     *
     * @throws InvalidHouseException if the range or playerNum are not in the right range
     *
     * @throws InvalidMoveException if  the house does not
     * represent a valid move because either the house is empty, or the move would leave the opponent without
     * a move to make
     *
     **/
    
    public BoardClass()
    {
        this.houses = new int[]{ 0,4,4,0,0,0,0,0,0,0,0,0 };
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
        System.out.print('\u000C');
        System.out.println(this.toString());
    }
    
    
    /**
     * the number of seeds in the specified house of the specified player. See {@link Board#makeMove makeMove()} for parameterDetails
     *
     */
    public int getSeeds(int house, int playerNum) throws InvalidHouseException
    {
        if(house < 1 || house > 6 || playerNum < 1 || playerNum > 2) throw new InvalidHouseException("Invalid house number.");
        int seeds = this.houses[(playerNum-1)*6 + house-1];         
        return seeds;
    }
    
    /**
     * sow a seed in a location: increase the number of seeds already there by one
     **/
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
    
    /**
     * set the number of seeds in a house to a given value
     */
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
    
    /**
     * find the number of seeds in a player score house
     **/
    public int getScore(int playerNum)
    {
        if(playerNum == 1)
        {
            return this.scoreHouse1;
        }
        return this.scoreHouse2;
    }
    
    
    /**
     * increase a player's score by putting seeds into their score house
     **/
    public void addScore(int seeds, int playerNum)
    {
        if(playerNum == 1)
        {
            this.scoreHouse1 += seeds;
        }
        else
        {
            this.scoreHouse2 += seeds;
        }
    }

    
    /**
     * set the number of seeds in a player's score house
     */
    public void setScore(int seeds, int playerNum)
    {
        if(playerNum == 1)
        {
            this.scoreHouse1 = seeds;
        }
        this.scoreHouse2 = seeds;
    }
        
    
    /**
     * override the toString method to provide a summary of the board state
     **/
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

    /**
     * override the clone method to copy a board state that can be passed to a player;
     **/
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

    /**
     * override equals
     *
     **/

    public boolean equals(Object o){   return false; }
}
