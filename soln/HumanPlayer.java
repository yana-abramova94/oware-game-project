import java.util.Scanner;
import java.io.*;
/**
 *
 * @author Yana Abramova
 * @version 1.0
 *
 */
public class HumanPlayer implements Player
{
    private String name;
    private InputStream in;
    private PrintStream out;
    public HumanPlayer()
    {
        this.setIn(System.in);
        this.setOut(System.out);
    }

    public int getMove(Board b, int playerNum) throws QuitGameException, IllegalStateException
    {
        if(b == null) throw new IllegalStateException("The argument Board b is null");
        if(playerNum != 1 && playerNum != 2) throw new IllegalStateException("The argument int playerNum is different from 1 and 2");
        String input = new String();
        Scanner scan = new Scanner(this.in);
        this.out.println("Player " + (!this.name.equals("") ? this.name : playerNum) + ", please select the house number:");
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
                    this.out.println("Invalid input: please select the house number within 1 and 6:\n");
                }
            }
            catch(NumberFormatException e)
            {
                this.out.println("Invalid input: a valid input would consist of numbers from 1 to 6 or QUIT.\n");
                invalid = true;
            }
            
        } while(move < 1 || move > 6 || invalid);
        return move;
    }

    public boolean isComputer()
    {
        return false;
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
