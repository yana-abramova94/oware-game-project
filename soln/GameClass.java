import java.util.ArrayList;
/**
 * Write a description of class GameClass here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GameClass implements Game
{
    private Player currentPlayer;
    private Player player1;
    private Player player2;
    private int currentPlayerNum;
    private Board currentBoard;
    private ArrayList<String> boardHistory;
    
    public GameClass()
    {
        this.currentBoard = new BoardClass();
        this.boardHistory = new ArrayList<String>();        
    }
    
    public boolean isComputerPlayer(int n)
    {
        if(n == 1) return this.player1.isComputer();
        return this.player2.isComputer();
    }
    
    public Player getCurrentPlayer()
    {
        return this.currentPlayer;
    }
        
    public int getCurrentPlayerNum()
    {
        return this.currentPlayerNum;
    }
    
    public void setCurrentPlayer(int n)
    {
        this.currentPlayerNum = n;
        this.currentPlayer = (n == 1) ? this.player1 : this.player2;
    }
    
    public void setCurrentBoard(Board board)
    {
        this.currentBoard = board;
    }
    
    public Board getCurrentBoard()
    {
        return this.currentBoard;
    }
    
    
    /**
     @return 1 or 2 corresponding to the winning player, if the game is over and won. Return 0 if the game is over and it is a draw. Return a negative value if the game is not over.
     **/
    
    public int getResult()
    {
        if(this.currentBoard.getScore(1) >= 25)
        {
            return 1;
        } 
        else if(this.currentBoard.getScore(2) >= 25)
        {
            return 2;
        }
        else if(this.currentBoard.getScore(1) == 24 && this.currentBoard.getScore(2) == 24)
        {
            return 0;
        }
        else
        {
            return -1;
        }
    }
    
    /**
     * @return true if the current board position has occurred before in the history of the game.
     **/
    
    public boolean positionRepeated()
    {
        boolean repeated = false;
        for(String s : this.boardHistory)
        {
            if(s.equals(this.currentBoard.toString())) 
            {
                repeated = true;
            }
        }
        this.boardHistory.add(this.currentBoard.toString());
        System.out.println(this.boardHistory.size());
        return repeated;
    }
    
    /**
     * Get a move from the current player and update the board and current player
     */
    public void nextMove() throws InvalidHouseException, InvalidMoveException, QuitGameException
    {
        int selectedHouse = this.getCurrentPlayer().getMove(this.currentBoard, this.getCurrentPlayerNum());
        this.currentBoard.makeMove(selectedHouse, this.getCurrentPlayerNum());
        this.currentPlayerNum = (2-this.currentPlayerNum) + 1;
        this.currentPlayer = this.currentPlayerNum == 1 ? player1 : player2;
    }
    
    /**
     * override the toString method to provide a summary of the game state (including the board)
     **/
    public String toString()
    {
        String state = new String();
        if(this.getResult() == 1)
        {
            state += "Game ended. Winner: player 1!";
        }
        else if(this.getResult() == 2)
        {
            state += "Game ended. Winner: player 2!";
        }
        else if(this.getResult() == 0)
        {
            state += "Game ended. Draw!";
        } 
        else
        {
            state += "Game is in process. Current player: " + this.currentPlayerNum;
        }
        state += "\n";
        state += this.currentBoard.toString();
        return state;
    }
    
    public void setPlayer(int playerNum, Player p)
    {
        if(playerNum == 1) this.player1 = p;
        else this.player2 = p;
    }
}
