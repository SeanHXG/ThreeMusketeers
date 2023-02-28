package threeMusketeers;

import java.util.Stack;

// COMMAND PATTERN: MOMENTO
// Originator of GameState Objects
public class GameStateManager {
	
	private Stack<GameState> gameHist;
	
    /**
     * Initialize a new GameStateManager.
     */
	public GameStateManager() {
		this.gameHist = new Stack<>();
	}
	
	/**
     * Saves a GameState into gameHist.
     * @param state GameState to be saved.
     */
	public void save(GameState state) {
		gameHist.push(state);
	}
	
    /**
     * Reverts the current board to the state stored in gameHist.
     * @param board the current Board.
     */
	public void revert(Board board) {
		if (!this.gameHist.isEmpty()) {
			System.out.println("Restarting");
			board.restore(gameHist.pop());
		}
		else {
			System.out.println("There is nothing to restart");
		}
	}
}
