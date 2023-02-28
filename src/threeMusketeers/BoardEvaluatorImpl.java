package threeMusketeers;

import java.util.List;

public class BoardEvaluatorImpl implements BoardEvaluator {

    /**
     * Calculates a score for the given Board, based on musketeer positions
     * and number of possible musketeer moves. A higher score means the
     * Musketeer player is winning, while a lower score means the Guard player
     * is winning.
     * @param board Board to calculate the score of.
     * @return int Score of the given Board.
     */
    @Override
    public int evaluateBoard(Board board) { 
    	List<Cell> musketeers = board.getMusketeerCells();
    	int inRow = 1;
    	int inCol = 1;
        // Check if the second or third musketeers are in the same row or column as the first
    	for (int n = 1; n < 3; n++) {
    		if (musketeers.get(0).getCoordinate().row == musketeers.get(n).getCoordinate().row) {
    			inRow *= 2;
    		}
    		else if (musketeers.get(0).getCoordinate().col == musketeers.get(n).getCoordinate().col) {
    			inRow *= 2;
    		}
    	}
        // Check if the second musketeer is in the same row or column as the third
    	if (musketeers.get(1).getCoordinate().row == musketeers.get(2).getCoordinate().row) {
			inCol *= 2;
		}
		else if (musketeers.get(1).getCoordinate().col == musketeers.get(2).getCoordinate().col) {
			inCol *= 2;
		}
    	
        return board.numMusketeerMoves() - (5*Math.max(inRow, inCol));
    }
}