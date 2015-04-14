import java.util.ArrayList;
/**
 * 
 * @author Yana Abramova
 * @version 1.0
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
    
    public boolean isComputerPlayer(int n) throws IllegalStateException
    {
        if(n != 1 && n != 2) throw new IllegalStateException("The argument int n is different from 1 and 2");
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
    
    public void setCurrentPlayer(int n) throws IllegalStateException
    {
        if(n != 1 && n != 2) throw new IllegalStateException("The argument int n is different from 1 and 2");
        this.currentPlayerNum = n;
        this.currentPlayer = (n == 1) ? this.player1 : this.player2;
    }
    
    public void setCurrentBoard(Board board) throws IllegalStateException
    {
        if(board == null) throw new IllegalStateException("The argument Board board is null");
        this.currentBoard = board;
    }
    
    public Board getCurrentBoard()
    {
        return this.currentBoard;
    }
    
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
    
    public void nextMove() throws InvalidHouseException, InvalidMoveException, QuitGameException
    {
        int selectedHouse = this.getCurrentPlayer().getMove(this.currentBoard, this.getCurrentPlayerNum());
        this.currentBoard.makeMove(selectedHouse, this.getCurrentPlayerNum());
        this.currentPlayerNum = (2-this.currentPlayerNum) + 1;
        this.currentPlayer = this.currentPlayerNum == 1 ? player1 : player2;
    }

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
    
    public void setPlayer(int playerNum, Player p) throws IllegalStateException
    {
        if(playerNum != 1 && playerNum != 2) throw new IllegalStateException("The argument int playerNum is different from 1 and 2");
        if(p == null) throw new IllegalStateException("The argument Player p is null");
        if(playerNum == 1) this.player1 = p;
        else this.player2 = p;
    }
}
