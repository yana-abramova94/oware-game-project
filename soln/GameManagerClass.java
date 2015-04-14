import java.util.Comparator;
import java.io.*;
import java.util.Scanner;
/**
 *
 * @author Yana Abramova
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
    
    public void setGame(GameClass game) throws IllegalStateException
    {
        if(game == null) throw new IllegalStateException("The argument GameClass game is null");
        this.game = game;
    }
    
    public void main(String args)
    {
        int state = -1;
        while(true)
        {
            GameClass game = new GameClass();
            System.out.print("\u000C");
            if(args == null || args.equals(""))
            {
                System.out.println("Welcome to the Oware Game!");
                System.out.println("\t*** MAIN MENU ***");
                System.out.println(" * NEW");
                System.out.println(" * SAVE");
                System.out.println(" * LOAD");
                System.out.println(" * EXIT");
                game = (GameClass)this.manage(System.in, System.out);
                this.game = game;
            }
            else 
            {
                try
                {
                    this.loadGame(args);
                }
                catch(FileFailedException ex)
                {
                    
                    System.out.println(ex.getMessage() + " " + args);
                    System.out.println("Press any key to proceed to main menu...");
                    args = "";
                    try
                    {
                        System.in.read();
                    }  
                    catch(Exception e)
                    {} 
                    continue;
                }
                args = "";
            }
            this.playMatch(this,state);
        }
    }
    
    public int playMatch(GameManagerClass gameManager, int state) throws IllegalStateException
    {
        if(gameManager == null) throw new IllegalStateException("The argument GameManagerClass gameManager is null");
        if(state != -1 && state != 0 && state != 1 && state != 2) throw new IllegalStateException("The argument int state is different from -1, 0, 1 or 2");
        if(gameManager.game != null) 
        {
            try
            {
                state = gameManager.playGame();
            }
            catch(QuitGameException ex)
            {
                System.out.println(ex.getMessage());
            }
            
            if(state == 0) System.out.println("Draw!\n");
            else if(state == 1 || state == 2) System.out.println("Player " + state + " wins!\n");
            else System.out.println("Game is not finished. You can save it by the command 'SAVE _fname_'");
            String input = new String();
            Scanner scan = new Scanner(System.in);
            System.out.println("Press any key to go to main menu...");
            input = scan.nextLine();
        }
        return state;
    }
    
    public void loadGame(String fname) throws FileFailedException, IllegalStateException
    {
        if(fname == null) throw new IllegalStateException("The argument String fname is null");
        if(fname.equals("")) throw new IllegalStateException("The argument String fname is empty");
        this.game = new GameClass();
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
           int currentPlayer = Integer.parseInt(reader.readLine());
           this.game.setPlayer(1,reader.readLine().equals("true") ? new ComputerPlayer() : new HumanPlayer());
           this.game.setPlayer(2,reader.readLine().equals("true") ? new ComputerPlayer() : new HumanPlayer());
           this.game.setCurrentPlayer(currentPlayer);
           this.game.setCurrentBoard(board);
           reader.readLine();
           reader.readLine();
           reader.close();
        }
        catch(FileNotFoundException ex)
        {
            throw new FileFailedException("Cannot find or open the file");
        }
        catch(IOException ex)
        {
            throw new FileFailedException("Cannot find or open the file");
        }
        catch(InvalidHouseException ex)
        {
        }
    }
    
    public void saveGame(String fname) throws FileFailedException, IllegalStateException
    {
        if(fname == null) throw new IllegalStateException("The argument String fname is null");
        if(fname.equals("")) throw new IllegalStateException("The argument String fname is empty"); 
        try
        {
           FileOutputStream fos = new FileOutputStream(fname);
           OutputStreamWriter osw = new OutputStreamWriter(fos);
           BufferedWriter writer = new BufferedWriter(osw);
           String input = null;
           Board board = this.game.getCurrentBoard();
           input = board.getSeeds(1,1) + "\n" + board.getSeeds(2,1) + "\n" + board.getSeeds(3,1) + "\n" + board.getSeeds(4,1) + "\n" 
                 + board.getSeeds(5,1) + "\n" + board.getSeeds(6,1) + "\n" + board.getSeeds(1,2) + "\n" + board.getSeeds(2,2) + "\n"
                 + board.getSeeds(3,2) + "\n" + board.getSeeds(4,2) + "\n" + board.getSeeds(5,2) + "\n" + board.getSeeds(6,2) + "\n"
                 + board.getScore(1) + "\n" + board.getScore(2) + "\n" + this.game.getCurrentPlayerNum() + "\n" + this.game.isComputerPlayer(1) + "\n" + this.game.isComputerPlayer(2);
           writer.write(input);
           writer.close();
           osw.close();
           fos.close();
        }
        catch(Exception ex)
        {
            throw new FileFailedException("Cannot find or open the file");
        }
    }

    public int playGame() throws QuitGameException
    {
        System.out.print('\u000C');
        System.out.println(this.game.getCurrentBoard().toString());
        int state = this.game.getResult();
        int previousScore1 = 0;
        int previousScore2 = 0;
        int noCapture = 0;
        while(state == -1)
        {
            try
            {
                boolean isComputer = this.game.getCurrentPlayer().isComputer();
                long startTime = System.nanoTime();
                this.game.nextMove();
                long estimatedTime = System.nanoTime() - startTime;
                if(isComputer)
                {
                    if(estimatedTime > 1000000000) 
                    {
                        state = this.game.getCurrentPlayerNum();
                        System.out.println("It took more than one second for computer player to make a move");
                        break;
                    }
                }
                if(this.game.positionRepeated())
                {
                    int seeds = 0;
                    for(int i = 1; i < 7; i++)
                    {
                        seeds = this.game.getCurrentBoard().getSeeds(i,1);
                        this.game.getCurrentBoard().setSeeds(0,i,1);
                        this.game.getCurrentBoard().addScore(seeds,1);
                        seeds = this.game.getCurrentBoard().getSeeds(i,2);
                        this.game.getCurrentBoard().setSeeds(0,i,2);
                        this.game.getCurrentBoard().addScore(seeds,2);
                    }
                    state = this.game.getCurrentBoard().getScore(1) > this.game.getCurrentBoard().getScore(2) ? 1 : 2;
                    state = this.game.getCurrentBoard().getScore(1) == 24 && this.game.getCurrentBoard().getScore(2) == 24 ? 0 : state;
                    System.out.print('\u000C');
                    System.out.println(this.game.getCurrentBoard().toString());
                    System.out.println("Position repeated! Capturing all seeds from the board to respective players");
                    break;
                }
                if(this.game.getCurrentBoard().getScore(1) == previousScore1 && this.game.getCurrentBoard().getScore(2) == previousScore2)
                {
                    noCapture++;
                }
                else
                {
                    previousScore1 = this.game.getCurrentBoard().getScore(1);
                    previousScore2 = this.game.getCurrentBoard().getScore(2);
                    noCapture = 0;
                }
                if(noCapture == 100)
                {
                    state = previousScore1 > previousScore2 ? 1 : 2;
                    state = previousScore1 == previousScore2 ? 0 : state;
                    System.out.println("100 moves with no capture!");
                }
                else  state = this.game.getResult();
                System.out.print('\u000C');
                System.out.println(this.game.getCurrentBoard().toString());
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
        return state;
    }
    
    public Game manage(InputStream in, PrintStream out) throws IllegalStateException
    {
        String fname = "";
        if(in == null) throw new IllegalStateException("The argument InputStream in is null");
        if(out == null) throw new IllegalStateException("The argument PrintStream out is null");
        boolean invalid = true;
        try
        {
            while(invalid)
            {
                String input = new String();
                Scanner scan = new Scanner(in);
                input = scan.nextLine();
                String first = "", second = "", firstName = "", secondName = "";
                String[] words = input.split(" "); 
                if(words != null && words.length == 3 && words[0].equals("NEW")) 
                {
                    int colon = words[1].indexOf(':');
                    if(colon!=-1) 
                    {
                        first = words[1].substring(0,colon);
                        firstName = words[1].substring(colon + 1);
                    }
                    else first = words[1];
                    colon = words[2].indexOf(':');
                     if(colon!=-1) 
                    {
                        second = words[2].substring(0,colon);
                        secondName = words[2].substring(colon + 1);
                    }
                    else second = words[2];
                    if((first.equals("Human") || first.equals("Computer")) && (second.equals("Human") || second.equals("Computer"))) input = "NEW";
                }
                else if(input.length() > 7 && input.substring(0,4).equals("SAVE")) 
                {
                    fname = input.substring(5);
                    input = "SAVE";
                }
                else if(input.length() > 7 && input.substring(0,4).equals("LOAD")) 
                {
                    fname = input.substring(5);
                    input = "LOAD";
                }
                switch(input)
                {
                    case "LOAD":
                        this.loadGame(fname);
                        invalid = false;
                        break;
                    case "SAVE":
                        this.saveGame(fname);
                        invalid = false;
                        this.game = null;
                        break;
                    case "NEW":
                        this.game = new GameClass();
                        if(first.equals("Human"))
                        {
                            HumanPlayer player1 = new HumanPlayer();
                            player1.setName(firstName);
                            this.game.setPlayer(1, player1);
                            this.game.setCurrentPlayer(1);
                        }
                        else
                        {
                            ComputerPlayer player1 = new ComputerPlayer();
                            player1.setName(firstName);
                            this.game.setPlayer(1, player1);
                            this.game.setCurrentPlayer(1);
                        }
                        if(second.equals("Human"))
                        {
                            HumanPlayer player2 = new HumanPlayer();
                            player2.setName(secondName);
                            this.game.setPlayer(2, player2);
                        }
                        else
                        {
                            ComputerPlayer player2 = new ComputerPlayer();
                            player2.setName(secondName);
                            this.game.setPlayer(2, player2);
                        }
                        invalid = false;
                        break;
                    case "EXIT":
                        System.exit(0);
                        break;
                    default:
                        out.print("Invalid input: choose between \n" 
                            +" 'NEW _player1type_ _player2type_' where the player types are either 'Human' or 'Computer'. The user may optionally specify player names with a colon but no space. E.g. 'NEW Human:Steven Computer:Orac'\n"
                            +" SAVE, where game can be saved with 'SAVE _fname_\n"
                            +" LOAD, where the user may load a previously saved game by entering 'LOAD _fname_' where _fname_ is the name of a previously saved game file \n"
                            +" EXIT\n"); 
                        break;
                }
            }
        }
        catch(FileFailedException ex)
        {
            System.out.println(ex.getMessage() + " " + fname);
            System.out.println("Press any key to proceed to main menu...");
            try
            {
                System.in.read();
            }  
            catch(Exception e)
            {} 
            return null;
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return this.game;
    }
    
    public int compare(Player p1, Player p2) throws IllegalStateException
    {
        if(p1 == null) throw new IllegalStateException("The argument Player p1 is null");
        if(p2 == null) throw new IllegalStateException("The argument Player p2 is null");
        GameManagerClass gameManager = new GameManagerClass();
        GameClass game = new GameClass();
        game.setPlayer(1, p1);
        game.setPlayer(2, new ComputerPlayer());
        game.setCurrentPlayer(1);
        gameManager.setGame(game);
        int winner1 = gameManager.playMatch(gameManager,-1);
        game = new GameClass();
        game.setPlayer(1, p2);
        game.setPlayer(2, new ComputerPlayer());
        game.setCurrentPlayer(1);
        gameManager.setGame(game);
        int winner2 = gameManager.playMatch(gameManager,-1);
        if(winner1 == winner2) return 0;
        if(winner1 > winner2) return 1;
        if(winner2 > winner1) return 2;
        return -1;
    }
}
