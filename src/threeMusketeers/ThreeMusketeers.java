package threeMusketeers;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ThreeMusketeers {
	
	private Board board;
    private Agent musketeerAgent, guardAgent;
    private final Scanner scanner = new Scanner(System.in);
    private final List<Move> moves = new ArrayList<>();
    private ReplayGame replay = null;
    private  List<Move> replayList = new ArrayList<>();
    private String boardName;
    private GameStateManager originalBoard = new GameStateManager();
    private GameManager manager = new GameManager();
    private PlayerRecords records = new PlayerRecords();
    private String muskPlayer;
    private String guardPlayer;
    private PlayerRankings playerRanking = new PlayerRankings();

    // All possible game modes
    public enum GameMode {
        Human("Human vs Human"),
        HumanRandom("Human vs Computer (Random)"),
        HumanGreedy("Human vs Computer (Greedy)"),
    	Replay("Replay a Game");

        private final String gameMode;
        GameMode(final String gameMode) {
            this.gameMode = gameMode;
        }
    }

    /**
     * Default constructor to load the Default board
     */
    public ThreeMusketeers() {
        this.board = new Board();
        this.originalBoard.save(board.createState());
        this.loadPlayerRanking();
    }

    /**
     * Constructor to load a specific board
     * @param boardFilePath String filepath of the board
     */
    public ThreeMusketeers(String boardFilePath) {
        this.board = new Board(boardFilePath);
        this.originalBoard.save(board.createState());
        this.boardName = (boardFilePath);
        this.loadPlayerRanking();
    }
    
    /**
     * Loads all players from the current player registry
     */
    private void loadPlayerRanking() {
    	Set<String> players =  records.getRegisteredPlayers().keySet();
    	for (String playerName: players) {
    		Player player = records.getRegisteredPlayers().get(playerName);
    		this.playerRanking.addObserver(player);
    		this.playerRanking.addPlayer(player);
    	}
    }

    /**
     * Ask the user for a gamemode, and begin a new game with
     * that gamemode.
     */
    public void play(){
        System.out.println("Welcome! \n");
        final GameMode mode = getModeInput();
        System.out.println("Playing " + mode.gameMode);
        play(mode);
    }

    /**
     * Begin a new game with a given gamemode.
     * @param mode GameMode to run
     */
    public void play(GameMode mode){
        selectMode(mode);
        runGame();
    }

    /**
     * Sets the correct agents based on the given GameMode.
     * @param mode GameMode which is selected
     */
    private void selectMode(GameMode mode) {
        switch (mode) {
            case Human -> {
                System.out.println("Please select Musketeer Player: ");
                muskPlayer = selectPlayerProfile();
                System.out.println("");
                System.out.println("Please select Guard Player: ");
                guardPlayer = selectPlayerProfile();
                musketeerAgent = new HumanAgent(board);
                guardAgent = new HumanAgent(board);
            }
            case HumanRandom -> {
                String side = getSideInput();
                if (side.equals("M") ){
                    musketeerAgent = new HumanAgent(board);
                    guardAgent = new RandomAgent(board);
                }
                else{
                    musketeerAgent = new RandomAgent(board);
                    guardAgent = new HumanAgent(board);
                }
            }
            case HumanGreedy -> {
                String side = getSideInput();
                if (side.equals("M") ){
                    musketeerAgent = new HumanAgent(board);
                    guardAgent = new GreedyAgent(board);
                }
                else{
                    musketeerAgent = new GreedyAgent(board);
                    guardAgent = new HumanAgent(board);
                }
            }
            case Replay -> {
            	replayList = board.loadReplay(this.selectReplay());
            	replay = new ReplayGame(this.board, this.replayList);
            	System.out.println("hello");
            }
        }
    }
    
    /**
     * Prompts the user to select a player from the currently
     * registered players.
     * @return the name of the player whom is selected by the user.
     */
    private String selectPlayerProfile() {
    	Set<String> playerList = records.getRegisteredPlayers().keySet();
    	ArrayList<String> tempPlayerName = new ArrayList<>();
    	
    	int pos = 0;
    	for (String playerName: playerList) {
    		System.out.println(pos + ". " + playerName);
    		tempPlayerName.add(playerName);
    		pos++;
    	}	
    	
    	System.out.print("Enter a number: ");
    	while (!scanner.hasNextInt()) {
            System.out.print("Invalid option, please enter a number");
            scanner.next();
        }

    	int selectedNum = scanner.nextInt();
    	if (selectedNum < 0 || selectedNum > tempPlayerName.size() - 1) {
    		System.out.println("Invalid Player.");
    		return this.selectPlayerProfile();
    	}
    	return tempPlayerName.get(selectedNum);
    }
    
    /**
     * Prompts the user to select a hint strategy from a list
     * of HumanAgent strategies, and returns which one they selected
     * @param curAgent HumanAgent, who will recieve hints
     * @return the name of the strategy the user selected
     */
    private String selectHintStrat(HumanAgent curAgent) {
    	System.out.println("Select a strategy: ");
    	ArrayList<String> tempStrat = new ArrayList<>();
    	int pos = 0;
    	for (String stratName: curAgent.getStrats().keySet()) {
    		System.out.println(pos + ". " + stratName);
    		tempStrat.add(stratName);
    		pos++;
    	}	
    	System.out.print("Enter a number: ");
    	while (!scanner.hasNextInt()) {
            System.out.print("Invalid option, please enter a number");
            scanner.next();
        }

    	int selectedNum = scanner.nextInt();
    	if (selectedNum < 0 || selectedNum > tempStrat.size() - 1) {
    		System.out.println("Invalid strategy.");
    		return this.selectHintStrat(curAgent);
    	}
    	return tempStrat.get(selectedNum);
    }
    
    /**
     * Runs the game, handling human input for move actions
     * Handles moves for different agents based on current turn 
     */
    private void runGame() {
        while(!board.isGameOver()) {
            System.out.println("\n" + board);
            
            // Handles replays differently, since nobody is "playing" the game
            if (replay != null) {
            	replay.replay();
            	break;
            }
            
            else {
	
	            final Agent currentAgent;
	            if (board.getTurn() == Piece.Type.MUSKETEER)
	                currentAgent = musketeerAgent;
	            else
	                currentAgent = guardAgent;
	            
	            if (currentAgent instanceof HumanAgent) // Human move
	                switch (getInputOption()) {
	                    case "M":
	                        move(currentAgent);
	                        break;
	                    case "U":
	                        if (moves.size() == 0) {
	                            System.out.println("No moves to undo.");
	                            continue;
	                        }
	                        else if (moves.size() == 1 || isHumansPlaying()) {
	                            undoMove();
	                        }
	                        else {
	                            undoMove();
	                            undoMove();
	                        }
	                        break;
	                    case "S":
	                        board.saveBoard();
	                        break;
	                    case "R":
	                    	this.originalBoard.revert(board);
	                    	this.moves.clear();
	                    	break;
	                    case "H":
	                    	String hintStratName = selectHintStrat((HumanAgent) currentAgent);
	                    	((HumanAgent) currentAgent).getHint(hintStratName);
	                    	break;
	                }
	            else { // Computer move
	                System.out.printf("[%s] Calculating move...\n", currentAgent.getClass().getSimpleName());
	                move(currentAgent);
	            }
            }
        }

        System.out.println(board);
        System.out.printf("\n%s won!%n", board.getWinner().getType());

        if (replay == null) { // Should not save a replay or update players for replays
            // Update match history
            if (musketeerAgent instanceof HumanAgent && guardAgent instanceof HumanAgent) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                String date = LocalDate.now().format(formatter);
                if (board.getWinner().getType().equals("MUSKETEER")) {
                    Match match = new Match(muskPlayer, guardPlayer, muskPlayer, date);
                    records.getRegisteredPlayers().get(muskPlayer).addMatch(match);
                    records.getRegisteredPlayers().get(muskPlayer).addWin();
                    records.getRegisteredPlayers().get(guardPlayer).addMatch(match);
                } else {
                    Match match = new Match(muskPlayer, guardPlayer, guardPlayer, date);
                    records.getRegisteredPlayers().get(muskPlayer).addMatch(match);
                    records.getRegisteredPlayers().get(guardPlayer).addMatch(match);
                    records.getRegisteredPlayers().get(guardPlayer).addWin();
                }
                this.playerRanking.updateRanks();
                records.savePlayers();
            }
            // Saving replay
            ArrayList<String> sReplay = saveReplayInput();
        	if (sReplay.get(0).equals("Y")) {
        		board.saveReplay(boardName, sReplay.get(1), this.moves);
        	}
        }
        System.out.println("Thank you for playing THREE MUSKETEERS!");
        System.out.println("Exiting...");
    }

    /**
     * Gets a move from the given agent, adds a copy of the move using the copy constructor to the moves stack.
     * Then, performs the move on the board.
     * @param agent Agent to get the move from.
     */
    protected void move(final Agent agent) {
    	Move move = new Move(agent.getMove());
    	moves.add(move);
    	board.move(move);
    	
    }

    /**
     * Removes a move from the top of the moves stack and undoes the move on the board.
     */
    private void undoMove() { 
    	board.undoMove(moves.get(moves.size() - 1));
    	moves.remove(moves.size() - 1);
    }

    /**
     * Get human input for a move action
     * @return a character representing the move action: "M" - Move, "U" - Undo, "S" - Save, "H" - Hint, "R" - Restart
     */
    private String getInputOption() {
        System.out.printf("[%s] Enter 'M' to move, 'U' to undo, 'S' to save, 'H' for hint and 'R' to restart game: ", board.getTurn().getType());
        while (!scanner.hasNext("[MUSRHmusrh]")) {
            System.out.print("Invalid option. Enter 'M', 'U', 'S', 'H' or 'R': ");
            scanner.next();
        }
        return scanner.next().toUpperCase();
    }
    
    /**
     * Get human input for saving a replay
     * If the user does not want to save a replay, the returned array will contain a single element "N".
     * If the user wants to save a replay, the returned array will contain two elements: "Y" and the file name for the replay.
     * @return An array containing the user's response.
     */
    private ArrayList<String> saveReplayInput() {
    	ArrayList<String> result = new ArrayList<String>();
        System.out.printf("Would you like to save the previous game to a replay file? Enter 'Y' or 'N': ");
        while (!scanner.hasNext("[YNyn]")) {
            System.out.print("Invalid option. Enter 'Y' or 'N': ");
            scanner.next();
        }
        result.add(scanner.next().toUpperCase());
        if (result.get(0).equals("Y")) {
        	System.out.printf("Enter the name of the file: ");
            result.add(scanner.next());
        }
        return result;
    }

    /**
     * Returns whether both sides are human players
     * @return True if both sides are Human, False if one of the sides is a computer
     */
    private boolean isHumansPlaying() {
        return musketeerAgent instanceof HumanAgent && guardAgent instanceof HumanAgent;
    }

    /**
     * Get human input for side selection
     * @return the selected Human side for Human vs Computer games,  'M': Musketeer, G': Guard
     */
    private String getSideInput() {
        System.out.print("Enter 'M' to be a Musketeer or 'G' to be a Guard: ");
        while (!scanner.hasNext("[MGmg]")) {
            System.out.println("Invalid option. Enter 'M' or 'G': ");
            scanner.next();
        }
        return scanner.next().toUpperCase();
    }

    /**
     * Get human input for selecting the game mode. 
     * @return the chosen GameMode
     */
    private GameMode getModeInput() {
        System.out.println("""
                    0: Human vs Human
                    1: Human vs Computer (Random)
                    2: Human vs Computer (Greedy)
                    3: Replay a Game
                    4: View Player Profiles
                    5: Load Different Board
                    6: See Player Rankings
                    7: Create a Custom Board
                    """);
        System.out.print("Choose a game mode option or the load different board option (i.e. enter a number): ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid option. Enter 0, 1, 2, 3, 4, 5, 6, or 7: ");
            scanner.next();
        }
        final int mode = scanner.nextInt();
        if (mode < 0 || mode > 7) {
            System.out.println("Invalid option.");
            return getModeInput();
        }
        else if (mode == 7) {
        	BoardCreator creator = new BoardCreator();
        	creator.buildBoard();
        	return getModeInput();
        }
        else if (mode == 6) {
        	this.playerRanking.printRanking();
        	return getModeInput();
        }
        else if (mode == 5){
        	this.changeBoard();
        	return getModeInput();
        }
        else if (mode == 4) {
        	System.out.println("What would you like to do: ");
        	Integer res = selectProfileCommand();
        	if (res == 1) {
        		System.out.println("Please select a player from the list below:");
        		String selectedPlayer = selectPlayerProfile();
        		System.out.println("-----------------------------------------------\nViewing Player Profile:\n");
        		System.out.println(records.getRegisteredPlayers().get(selectedPlayer));
        		System.out.println("-----------------------------------------------");
        	} else if (res == 2){
        		System.out.println("Select one of the following commands: ");
        		Integer cmdNum = selectAddRemovePlayer();
        		System.out.println("-----------------------------------------------");
        		switch (cmdNum) {
        		case 0:
        			System.out.println("***Adding a new player***");
        			System.out.print("Enter a name: ");
        			String newname = scanner.next().toString();
        			Player player = new Player(newname);
        			this.playerRanking.addObserver(player);
            		this.playerRanking.addPlayer(player);
        			manager.modifyPlayerRegistry(new AddPlayerOrder(records, player));
        			System.out.println("-----------------------------------------------");
        			break;
        		case 1:
        			System.out.println("***Removing a player***");
        			System.out.println("Enter the name of the player you want to remove: ");
        			String removename = scanner.next().toString();
        			player = records.getRegisteredPlayers().get(removename);
        			this.playerRanking.removeObserver(player);
        			this.playerRanking.removePlayer(player);
        			manager.modifyPlayerRegistry(new RemovePlayerOrder(records, player));
        			System.out.println("-----------------------------------------------");
        			break;
        		case 2:
        			System.out.println("***Undoing last command***");
        			manager.undo();
        			System.out.println("-----------------------------------------------");
        			break;
        		}
        	}
        	
        	return getModeInput();
        }
        return GameMode.values()[mode];
        
    }
    
    /**
     * Prompts the user to select if they want to add a new player, remove an
     * existing one, or return to the previous menu, and returns the result.
     * @return an integer representing the user's choice.
     */
    private Integer selectAddRemovePlayer() {
    	System.out.println("0. Add a new player");
    	System.out.println("1. Remove a player");
    	System.out.println("2. Undo last command");
    	System.out.print("Enter a number: ");
    	while (!scanner.hasNextInt()) {
            System.out.print("Invalid option, please enter a number");
            scanner.next();
        }

    	int selectedNum = scanner.nextInt();
    	if (selectedNum < 0 || selectedNum > 3) {
    		System.out.println("Invalid Option.");
    		return this.selectProfileCommand();
    	}
    	return selectedNum;
    }
    
    /**
     * Prompts the user to select a profile menu option.
     * @return an integer representing the user's choice.
     */
    private Integer selectProfileCommand() {
    	System.out.println("1. View Player Profiles");
    	System.out.println("2. Add/Remove Players");
    	System.out.print("Enter a number: ");
    	while (!scanner.hasNextInt()) {
            System.out.print("Invalid option, please enter a number");
            scanner.next();
        }

    	int selectedNum = scanner.nextInt();
    	if (selectedNum < 0 || selectedNum > 2) {
    		System.out.println("Invalid Option.");
    		return this.selectProfileCommand();
    	}
    	return selectedNum;
    }
    
    /**
     * Prompts the user to select a board from a list of boards given by
     * getBoardsList(). Then, changes the current board to selected board
     * in the "Boards" directory.
     */
    private void changeBoard() {
    	ArrayList<String> boardList = this.getBoardsList();
    	
    	int fileNum = 1;
    	System.out.println("");
    	for (String file: boardList) {
    		System.out.println(fileNum + ": " + file);
    		fileNum++;
    	}	
    	System.out.println("Choose a board from its corresponding number");
    	System.out.println("");
    	
    	while (!scanner.hasNextInt()) {
            System.out.print("Invalid option, please enter a number.");
            scanner.next();
        }
    	
    	int selectedNum = scanner.nextInt() - 1;
    	if (selectedNum < 0 || selectedNum > boardList.size()) {
    		System.out.println("Invalid Board Number.");
    		this.changeBoard();
    	}
    	else {
    		String selectedBoard = boardList.get(selectedNum);
    		this.originalBoard.revert(board);
    		this.moves.clear();
    		this.board = new Board("boards/" + selectedBoard);
    		this.originalBoard.save(board.createState());
    		this.boardName = ("boards/" + selectedBoard);
    	}
    }
    
    /**
     * Gets a list of all board files in Boards directory.
     * @return an array, where each element is a name of a board in the Boards directory.
     */
    private ArrayList<String> getBoardsList() {
    	// Create an array containing files in boards folder
    	File folder = new File("Boards");
    	File[] files = folder.listFiles();
    	ArrayList<String> fileNames = new ArrayList<>();
    	
    	for (File file: files) {
    		String filename = file.getName();
    		if (!filename.contains("Template")) {
    			fileNames.add(filename);
    		}
    	}
    	
    	return fileNames;
    }
    
    /**
     * Prompts the user to select a replay from a list of replays given by
     * getReplaysList().
     * @return a string containing the path of the replay.
     */
    private String selectReplay() {
    	ArrayList<String> replayList = this.getReplaysList();

    	int fileNum = 1;
    	System.out.println("");
    	for (String file: replayList) {
    		System.out.println(fileNum + ": " + file);
    		fileNum++;
    	}	
    	System.out.println("Choose a replay from its corresponding number");
    	System.out.println("");

    	while (!scanner.hasNextInt()) {
            System.out.print("Invalid option, please enter a number");
            scanner.next();
        }

    	int selectedNum = scanner.nextInt() - 1;
    	if (selectedNum < 0 || selectedNum > replayList.size()) {
    		System.out.println("Invalid Replay Number.");
    		return this.selectReplay();
    	}
    	return ("replays/" + replayList.get(selectedNum));
    }


    /**
     * Gets a list of all replay files in replays directory.
     * @return an array, where each element is a name of a replay in the replay directory.
     */
    private ArrayList<String> getReplaysList() {
    	File folder = new File("replays");
    	File[] files = folder.listFiles();
    	ArrayList<String> fileNames = new ArrayList<>();

    	for (File file: files) {
    		String filename = file.getName();
    		fileNames.add(filename);
    	}

    	return fileNames;
    }

    public static void main(String[] args) {
        // Start ThreeMusketeers with the default board.
        String boardFileName = "boards/Default.txt";
        ThreeMusketeers game = new ThreeMusketeers(boardFileName);
        game.play();
    }
}