package threeMusketeers;

import java.util.ArrayList;

// COMMAND PATTERN: MOMENTO
// Stores the original state of a board
public class GameState {
    // Game board is a square
	 public final int size;
	 // 2D Array of Cells for representation of the game board
	 public final Cell[][] board;

	 private final Piece.Type turn;
	 private final Piece.Type winner;
	 
     /**
      * Initialize a new GameState with the given information
      * @param size int size of the board
      * @param board a Board
      * @param turn the player whose turn it is
      * @param winner the winner of the game, if any
      */
	 public GameState(int size, Cell[][] board, Piece.Type turn, Piece.Type winner) {
		this.size = size;
		this.board = board;
		this.turn = turn;
		this.winner = winner;
	 }
	 
     /**
      * A getter for all information stored within this GameState instance.
      * @return ArrayList<Object> containing information required to create a new board.
      */
	 public ArrayList<Object> getContent() {
		ArrayList<Object> content = new ArrayList<>(); 
		content.add(this.size);
		content.add(this.board);
		content.add(this.turn);
		content.add(this.winner);
		return content;
	 }
}
