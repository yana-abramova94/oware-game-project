import java.util.Comparator;
import java.io.*;
import java.util.Scanner;
/**
 *
 * GameManager objects allow the user to start, load, save and play game(s). In the main method, interaction with the game user is via the console.
 *
 * You need to provide a main method where the first command line parameter can optionally specify a saved game to load ({@link #loadGame loadGame()}) and play ({@link #playGame playGame()}).
 *
 * If a game is not underway the user may start a new game by entering 'NEW _player1type_ _player2type_' where the player types are either 'Human' or 'Computer'. The user may optionally specify player names with a colon but no space. E.g. 'NEW Human:Steven Computer:Orac' will start a new game between a human player called 'Steven' and a computer player called 'Orac'.
 *
 * If a game is not underway the user may load a previously saved game by entering 'LOAD _fname_' where _fname_ is the name of a previously saved game file ({@link #loadGame loadGame()}). The game is then restarted ({@link #playGame playGame()}).
 *
 * If a game is not underway but a game has been set up through LOAD or NEW or command line parameter then the game can be saved with 'SAVE _fname_'.
 *
 * If a game is not underway (e.g. because the game has finished) then the command 'EXIT' halts the program.
 
 * The GameManager also implements Comparator on Player by playing two games, with the two players taking turns to start. If the two games yield symmetric results (e.g. player 1 wins, player 2 wins) the the players are considered equal, otherwise the better player is better ...
 *
 * You need to provide a default constructor (no parameters) for a GameManager.
 *
 * @author Steven Bradley
 * @version 1.0
 *
 */

public class GameManagerClass implements GameManager
{
    private GameClass game;
    public GameManagerClass()
    {
        this.game = new GameClass();
    }
    /**
     * @return The current game object 
     */ 
    public Game getGame()
    {
        return this.game;
    }
    
    public void main(String[] args)
    {   
        while(true)
        {
            System.out.print("\u000C");
            System.out.println("Welcome to the Oware Game!");
            System.out.println("\t*** MAIN MENU ***");
            System.out.println(" * NEW");
            System.out.println(" * SAVE");
            System.out.println(" * LOAD");
            System.out.println(" * EXIT");
            this.manage(System.in, System.out);
            try
            {
                int state = this.playGame();
            }
            catch(QuitGameException ex)
            {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    /**
     * Load the game state from the given file.
     *
     * @param fname The name of the file to load.
     *
     * @throws FileFailedException If, for whatever reason, the game file could not be loaded.
     *
     */
    
    public void loadGame(String fname) throws FileFailedException
    {
        Board board = new BoardClass();
        try
        {
           FileInputStream fos = new FileInputStream(fname);
           BufferedReader reader = new BufferedReader(new InputStreamReader(fos));
		   board.setSeeds(Integer.parseInt(reader.readLine()),1,1);
		   board.setSeeds(Integer.parseInt(reader.readLine()),2,1);
		   board.setSeeds(Integer.parseInt(reader.readLine()),3,1);
		   board.setSeeds(Integer.parseInt(reader.readLine()),4,1);
		   board.setSeeds(Integer.parseInt(reader.readLine()),5,1);
		   board.setSeeds(Integer.parseInt(reader.readLine()),6,1);
		   board.setSeeds(Integer.parseInt(reader.readLine()),1,2);
		   board.setSeeds(Integer.parseInt(reader.readLine()),2,2);
		   board.setSeeds(Integer.parseInt(reader.readLine()),3,2);
		   board.setSeeds(Integer.parseInt(reader.readLine()),4,2);
		   board.setSeeds(Integer.parseInt(reader.readLine()),5,2);
		   board.setSeeds(Integer.parseInt(reader.readLine()),6,2);
		   board.setScore(Integer.parseInt(reader.readLine()),1);
		   board.setScore(Integer.parseInt(reader.readLine()),2);
		   this.game.setCurrentPlayer(Integer.parseInt(reader.readLine()));
		   this.game.setCurrentBoard(board);
		   reader.readLine();
		   reader.close();
		}
		catch(Exception ex)
		{
		    throw new FileFailedException("Cannot find or open the file");
		}
		
    }
    
    /**
     * Save the game state to the given file.
     *
     * @param fname The name of the file to save.
     *
     * @throws FileFailedException If, for whatever reason, the game file could not be saved (including file already exists).
     *
     */
    
    public void saveGame(String fname) throws FileFailedException
    {
        try
        {
           FileOutputStream fos = new FileOutputStream(fname);
           BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
           String input = null;
           Board board = this.game.getCurrentBoard();
           input = board.getSeeds(1,1) + "\n" + board.getSeeds(2,1) + "\n" + board.getSeeds(3,1) + "\n" + board.getSeeds(4,1) + "\n" 
                 + board.getSeeds(5,1) + "\n" + board.getSeeds(6,1) + "\n" + board.getSeeds(1,2) + "\n" + board.getSeeds(2,2) + "\n"
                 + board.getSeeds(3,2) + "\n" + board.getSeeds(4,2) + "\n" + board.getSeeds(5,2) + "\n" + board.getSeeds(6,2) + "\n"
                 + board.getScore(1) + "\n" + board.getScore(2) + "\n" + this.game.getCurrentPlayerNum();
		   writer.write(input);
		   writer.close();
		}
		catch(Exception ex)
		{
		    throw new FileFailedException("Cannot find or open the file");
		}
    }
    

    /**
     * Play the current game to completion, returning the playerNum of the winning player (1..2) or 0 if the game ends in a draw. Input is taken from in ({@link #manage manage()}). If a computer player quits the game then they lose. If a computer player takes longer than one second for any move then it loses. After each turn is taken the game state (toString()) is sent to out.
     
     * @throws QuitGameException If a human player quits the game via QuitGameException.
     **/
    
    public int playGame() throws QuitGameException
    {
        System.out.print('\u000C');
        System.out.print(this.game.getCurrentBoard().toString());
        int state = this.game.getResult();
        while(state == -1)
        {
            try
            {
                this.game.nextMove();
                state = this.game.getResult();
            }
            catch(InvalidHouseException ex)
            {
                System.out.println(ex.getMessage());
            }
            catch(InvalidMoveException ex)
            {
                System.out.println(ex.getMessage());
            }
        }            
        if(state == 0) System.out.println("Draw!");
        else System.out.println("Player " + state + " wins!");
        return this.game.getResult();
    }
    
    /**
     * accept input commands, including LOAD, SAVE, NEW, EXIT from the specified InputStream. All output is sent to the specified PrintStream. Can be used for testing the gameManager class via predefined inputs (e.g. a file) and writing the output to file. The specified input stream and output streams are also used for any player move input and output (i.e. selecting moves and QUIT). If a result is achieved this should be announced to the user.
     *
     * @param in The InputStream to be used for setting up and playing the game: System.in when not testing.
     *
     * @param out The OutputStream to be used for sending messages to the user while setting up and playing the game: System.out when not testing.
     *
     * @return The Game state after the instructions have been followed.
     **/
    public Game manage(InputStream in, PrintStream out) 
    {
        boolean invalid = true;
        try
        {
            while(invalid)
            {
                String input = new String();
                Scanner scan = new Scanner(in);
                input = scan.nextLine();
                switch(input)
                {
                    case "LOAD":
                        out.print("Write the path of the file where the game is saved: ");
                        this.loadGame(scan.nextLine());
                        invalid = false;
                        break;
                    case "SAVE":
                        out.print("Write the path of the file where the game is going to be saved: ");
                        this.saveGame(scan.nextLine());
                        invalid = false;
                        break;
                    case "NEW":
                        this.game = new GameClass();
                        invalid = false;
                        break;
                    case "EXIT":
                        System.exit(0);
                        break;
                    default:
                        out.print("Invalid input: choose between LOAD, SAVE, NEW or EXIT\n"); 
                        break;
                }
            }
        }
        catch(FileFailedException ex)
        {
            out.print(ex.getMessage());
        }
        return new GameClass();
    }
    
    public int compare(Player p1, Player p2)
    {
        return 0;
    }
}
