package threeMusketeers;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

import Exceptions.InvalidMoveException;
import threeMusketeers.Piece.Type;



public class Board {
    // Game board is a square
    public int size = 5;
    // 2D Array of Cells for representation of the game board
    public Cell[][] board = new Cell[size][size];

    private Piece.Type turn;
    private Piece.Type winner;
    
    
    /**
     * Functions to create new boards, based on the given board's file path. If no path is specified, loads the default board.
     */
    public Board() {
        this.loadBoard("boards/default.txt");
    }
    /**
     * @param boardFilePath String containing the path to the chosen board to be imported.
     */
    public Board(String boardFilePath) {
        this.loadBoard(boardFilePath);
    }

    /**
     * Creates a Board copy of the given board.
     * @param board Board to copy
     */
    public Board(Board board) {
        this.size = board.size;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                this.board[row][col] = new Cell(board.board[row][col]);
            }
        }
        this.turn = board.turn;
        this.winner = board.winner;
    }
    
    public Board(Cell[][] cells) {
        this.board = cells;
        this.size = cells.length;
        this.turn = Piece.Type.MUSKETEER;
        this.winner = null;
    }

    /**
     * @return the Piece.Type (Muskeeteer or Guard) of the current turn
     */
    public Piece.Type getTurn() {
        return turn;
    }

    /**
     * Get the cell located on the board at the given coordinate.
     * @param coordinate Coordinate of the desired cell
     * @return Cell that is located at the given coordinate
     */
    public Cell getCell(Coordinate coordinate) {
    	return board[coordinate.row][coordinate.col];
    }

    /**
     * @return the game winner Piece.Type (Muskeeteer or Guard) if there is one. Otherwise, returns null.
     */
    public Piece.Type getWinner() {
        return winner;
    }

    /**
     * Gets all cells containing a musketeer on the board.
     * @return List of cells
     */
    public List<Cell> getMusketeerCells() { 
    	List<Cell> result = new ArrayList<>();
    	for (int i = 0; i < this.size; i++) {
    		for (int n = 0; n < this.size; n++) {
    			if (board[i][n].hasPiece()) {
    				if (board[i][n].getPiece().getType().getType() == "MUSKETEER") {
    					result.add(board[i][n]);
    				}
    			}
    		}
    	}
        return result;
    }

    /**
     * Gets all cells containing a guard on the board.
     * @return List of cells
     */
    public List<Cell> getGuardCells() {
    	List<Cell> result = new ArrayList<>();
    	for (int i = 0; i < this.size; i++) {
    		for (int n = 0; n < this.size; n++) {
    			if (board[i][n].hasPiece()) {
    				if (board[i][n].getPiece().getType().getType() == "GUARD") {
    					result.add(board[i][n]);
    				}
    			}
    		}
    	}
        return result;
    }

    /**
     * Performs the given move on the board, then changes turns.
     * @param move a valid move
     */
    public void move(Move move) { 
    	Coordinate to = move.toCell.getCoordinate();
    	Coordinate from = move.fromCell.getCoordinate();
    	board[to.row][to.col].setPiece(move.fromCell.getPiece());
    	board[from.row][from.col].setPiece(null);
    	changeTurn();
    }

    /**
     * Undo the given move and revert the turn change.
     * @param move Copy of a move that was done and needs to be undone. The move copy has the correct piece info in the
     *             from and to cell fields.
     */
    public void undoMove(Move move) {
    	Coordinate to = move.toCell.getCoordinate();
    	Coordinate from = move.fromCell.getCoordinate();
    	board[to.row][to.col].setPiece(move.toCell.getPiece());
    	board[from.row][from.col].setPiece(move.fromCell.getPiece());
    	changeTurn();
    }
    
    /**
     * Changes whose turn it is.
     */
    public void changeTurn() {
    	if (this.getTurn().getType() == "MUSKETEER") {
    		this.turn = Piece.Type.GUARD;
    		return;
    	}
    	this.turn = Piece.Type.MUSKETEER;
    }
    
    /**
     * Checks if the given coordinate is on the board.
     * @param row int the row of the coordinate
     * @param col intthe column of the coordinate
     * @return True, if (row, col) is on the board, false otherwise.
     */
    
    public Boolean onBoard(int row, int col) {
    	try {
    		board[row][col].getCoordinate();
    	}
    	catch(Exception ArrayIndexOutOfBoundsExeption){
    		return false;
    	}
    	return true;
    }

    /**
     * Checks if the given move is valid.
     * @param move Move
     * @return True, if the move is valid; false otherwise
     */
    public Boolean isValidMove(Move move) {
    	Cell fromCell = move.fromCell;
    	Cell toCell = move.toCell;
    	
    	if (fromCell.getPiece() == null) {
    		return false;
    	}
    	else if (!(fromCell.getPiece().canMoveOnto(toCell))) {
    		return false;
    	}
    	else if (!(this.onBoard(move.toCell.getCoordinate().row, move.toCell.getCoordinate().col))) {
    		return false;
    	}
    	else if (!(getPossibleDestinations(fromCell).contains(toCell))) {
			return false;
		}
        return true;
    }

    /**
     * Get all the possible cells that have pieces that can be moved this turn.
     * @return An Array of Cells that contain a piece that can be moved this turn.
     */
    public List<Cell> getPossibleCells() {
    	List<Cell> result = new ArrayList<>();
    	
    	if (this.getTurn().getType() == "MUSKETEER") {
            List<Cell> mList = this.getMusketeerCells();
    		for (int n = 0; n < 3; n++) {
    			if (getPossibleDestinations(mList.get(n)).size() > 0) {
    				result.add(mList.get(n));
    			}
    		}
    	}
    	else {
            List<Cell> gList = this.getGuardCells();
    		for (int n = 0; n < gList.size(); n++) {
    			if (getPossibleDestinations(gList.get(n)).size() > 0) {
    				result.add(gList.get(n));
    			}
    		}
    	}
        return result;
    }

    /**
     * Get all the possible cell destinations for a move by the piece on fromCell.
     * @param fromCell Cell that has the piece that is going to be moved
     * @return List<Cell> a list of cells that are possible for the piece to move to.
     */
    public List<Cell> getPossibleDestinations(Cell fromCell) { 
    	List<Cell> result = new ArrayList<>();
    	Coordinate coord = fromCell.getCoordinate();
    	
    	for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if (!(x == y) && !(x == -y) && this.onBoard(coord.row + x, coord.col + y)) {
                    Cell check = board[coord.row + x][coord.col + y];
                    if (fromCell.getPiece().canMoveOnto(check)) {
                        result.add(check);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Get all the possible moves that can be made this turn.
     * @return List of moves that can be made this turn
     */
    public List<Move> getPossibleMoves() { 
    	List<Move> result = new ArrayList<>();
    	List<Cell> possible = this.getPossibleCells(); //Create an array with all cells that have pieces with possible moves
    	for (int n = 0; n < possible.size(); n++) {
    		List<Cell> moves = this.getPossibleDestinations(possible.get(n));
    		if (moves.size() > 0) {
    			for (int i = 0; i < moves.size(); i++) {
    				Move move = new Move(possible.get(n), moves.get(i));
    				result.add(move);
    			}
    		}
    	}
    	return result;
    }
    
    /**
     * Find how many moves the Musketeers have.
     * @return Number of moves that the musketeers have this turn.
     */
    public int numMusketeerMoves() { 
    	int result = 0;
    	
    	List<Cell> mList = this.getMusketeerCells();
		for (int n = 0; n < 3; n++) {
			result += getPossibleDestinations(mList.get(n)).size();
		}
    	return result;
    }

    /**
     * Checks if the game is over and sets the winner if there is one.
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver() {    	
    	Coordinate m1 = this.getMusketeerCells().get(0).getCoordinate();
    	Coordinate m2 = this.getMusketeerCells().get(1).getCoordinate();
    	Coordinate m3 = this.getMusketeerCells().get(2).getCoordinate();
    	if (((m1.row == m2.row) && (m1.row == m3.row)) || ((m1.col == m2.col) && (m1.col == m3.col))) {
			this.winner = Piece.Type.GUARD;
			return true;
    	}
    	else if (this.getTurn() == Piece.Type.MUSKETEER && this.getPossibleMoves().size() == 0) {
    		this.winner = Piece.Type.MUSKETEER;
    		return true;
    	}
    	return false;
    }
    
    /**
     * Saves the current board state to the boards directory, with the date as its title.
     */
    public void saveBoard() {
        String title = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        saveBoard(title);        
    }
    
    /**
     * Saves the current board state to the boards directory.
     * @param title the name of the board to be saved.
     */
    public void saveBoard(String title) {
    	String filePath = String.format("boards/%s.txt", title);
        File file = new File(filePath);

        try {
            file.createNewFile();
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(turn.getType() + "\n");
            for (Cell[] row: board) {
                StringBuilder line = new StringBuilder();
                for (Cell cell: row) {
                    if (cell.getPiece() != null) {
                        line.append(cell.getPiece().getSymbol());
                    } else {
                        line.append("_");
                    }
                    line.append(" ");
                }
                writer.write(line.toString().strip() + "\n");
            }
            writer.close();
            System.out.printf("Saved board to %s.\n", filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Failed to save board to %s.\n", filePath);
        }
    }
    
    /**
     * Saves the previous game's moves to the replays directory.
     * @param boardName String containing the path of the previous game's board  (e.g. "boards/Starter.txt")
     * @param fileName String containing the name to be given to the replay file  (e.g. "replay1")
     * @param moveList List<Move> representing the moves played by each player in the previous game.
     */
    public void saveReplay(String boardName, String fileName, List<Move> moveList) {
        String filePath = "replays/" + fileName + ".txt";
        File file = new File(filePath);

        try {
            file.createNewFile();
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(boardName + "\n");
            for (Move move: moveList) {
                StringBuilder line = new StringBuilder();
            	line.append(move.fromCell.getCoordinate().toString() + " " + move.toCell.getCoordinate().toString());
                writer.write(line.toString().strip() + "\n");
            }
            writer.close();
            System.out.printf("Saved replay to %s.\n", filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Failed to save replay to %s.\n", filePath);
        }
    }

    /**
     * Loads a board file from a file path.
     * @param filePath String containing the path to the board file to load (e.g. "boards/Starter.txt")
     */
    private void loadBoard(String filePath) {
        File file = new File(filePath);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.err.printf("File at %s not found.", filePath);
            System.exit(1);
        }

        turn = Piece.Type.valueOf(scanner.nextLine().toUpperCase());

        int row = 0, col = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] pieces = line.trim().split(" ");
            for (String piece: pieces) {
                Cell cell = new Cell(new Coordinate(row, col));
                switch (piece) {
                    case "O" -> cell.setPiece(new Guard());
                    case "X" -> cell.setPiece(new Musketeer());
                    case "=" -> cell.setPiece(new Blocker());
                    default -> cell.setPiece(null);
                }
                this.board[row][col] = cell;
                col += 1;
            }
            col = 0;
            row += 1;
        }
        System.out.printf("Loaded board from %s.\n", filePath);
    }
    /**
     * Loads a replay file from a file path. In this process, also sets up the board stated
     * in the replay file.
     * @param filePath String containing the path to the replay file to load (e.g. "Replay/replay1.txt")
     * @return ArrayList containing the moves in the given file.
     */
    public ArrayList<Move> loadReplay(String filePath) {
    	ArrayList<Move> moves = new ArrayList<Move>();
        File file = new File(filePath);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.err.printf("File at %s not found.", filePath);
            System.exit(1);
        }
        
        loadBoard(scanner.nextLine().toString().replace("\n", "")); // Loading the board
        
        while (scanner.hasNextLine()) { // While loop to load moves
            String line = scanner.nextLine();
            Coordinate fromCoord = null;
            Coordinate toCoord = null;
            
	    	try {
				fromCoord = Utils.parseUserMove(line.substring(0, 2));
				toCoord = Utils.parseUserMove(line.substring(3, 5));
		    	moves.add(new Move(getCell(fromCoord), getCell(toCoord)));
			} catch (InvalidMoveException e) {
				System.out.println("There was an error loading a move(s) in this replay file.");
				return null;
			}
        }

        System.out.printf("Loaded replay from %s.\n", filePath);
        return moves;
    }
    
    /**
     * Creates a new copy of the given cell
     * @param cell Cell to be copied
     * @return Cell the copy of the cell
     */
    public Cell copyCell(Cell cell) {
    	Cell copyCell = new Cell(cell);
    	return copyCell;
    }
    
    /**
     * Creates a new copy of the board
     * @return the copy of the board
     */
    public Cell[][] copyBoard(){
    	Cell[][] boardCopy = new Cell[size][size];
    	for (int row = 0; row < this.size; row++) {
    		for (int col = 0; col < this.size; col++) {
        		boardCopy[row][col] = this.copyCell(this.board[row][col]);
        	}
    	}
    	return boardCopy;
    }
    
    /**
     * Creates a GameState object containing the current state of the board
     * @return the new GameState object
     */
    public GameState createState() {
    	return new GameState(this.size, this.copyBoard(), this.turn, this.winner);
    }
    
    /** Restore a board to a given GameState
     * @param state GameState the board should be restored to.
     */
    public void restore(GameState state) {
    	ArrayList<Object> content = state.getContent();
    	this.size = (int) content.get(0);
    	this.board = (Cell[][]) content.get(1);
    	this.turn = (Type) content.get(2);
    	this.winner = (Type) content.get(3);
    }

    @Override
    public String toString() {
        StringBuilder boardStr = new StringBuilder("  | A B C D E\n");
        boardStr.append("--+----------\n");
        for (int i = 0; i < size; i++) {
            boardStr.append(i + 1).append(" | ");
            for (int j = 0; j < size; j++) {
                Cell cell = board[i][j];
                boardStr.append(cell).append(" ");
            }
            boardStr.append("\n");
        }
        return boardStr.toString();
    }

}
