package threeMusketeers;

import java.util.List;

public class ReplayGame {
	/*
	Instead of saving a text snapshot of every turn, this version of Three
	Musketeers saves the starting board of a match, and every move played in
	that match. When replaying, it loads the board and plays the game without
	player input, based on the moves list.
	*/ 
	
	Board board;
	ReplayCollection collection;
	
	/**
	 * Constructor for a new ReplayGame, with the given parameters.
	 * @param board Board the replay is played on.
	 * @param replayList List of moves to be played for the replay.
	 */
	public ReplayGame( Board board, List<Move> replayList) {
		this.board = board;
		this.collection = new ReplayCollection();
		for (Move move: replayList) {
			collection.addItem(move);
		}
	}
	
	/**
	 * Replays the match.
	 */
	public void replay() {
		ReplayIterator iterator = collection.createIterator();
		// Boolean temp = iterator.isDone();
		while (!(iterator.isDone())) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				System.out.println("There was an error with this replay");
			}
			board.move(iterator.currentItem());
			iterator.next();
			if (!board.isGameOver()) {
				System.out.println(board);
			}
		}
	}

}
