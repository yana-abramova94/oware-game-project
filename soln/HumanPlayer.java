import java.util.Scanner;
import java.io.*;
/**
 * Write a description of class HumanPlayer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HumanPlayer implements Player
{
    private InputStream in;
    private PrintStream out;
    public HumanPlayer()
    {
        this.setIn(System.in);
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
        String input = new String();
        Scanner scan = new Scanner(this.in);
        System.out.println("Player " + playerNum + ", please select the house number:");
        int move = 0;
        boolean invalid;
        do{
            invalid = false;
            input = scan.nextLine();
            if(input.equals("QUIT"))
            {
                throw new QuitGameException("Bye!");
            }
            try
            {
                move = Integer.parseInt(input);
                if(move < 1 || move > 6) 
                {
                    System.out.println("Invalid input: please select the house number within 1 and 6:\n");
                }
            }
            catch(NumberFormatException e)
            {
                System.out.println("Invalid input: a valid input would consist of numbers from 1 to 6 or QUIT.\n");
                invalid = true;
            }
            
        } while(move < 1 || move > 6 || invalid);
        return move;
    }
    
    /**
     * returns true is this is a computer player. Computer players are limited to one second per move (on E216 computers) and forfeit the game if they quit or make an invalid move.
     **/
    public boolean isComputer()
    {
        return false;
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
}
