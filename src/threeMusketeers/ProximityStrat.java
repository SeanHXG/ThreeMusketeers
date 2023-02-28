package threeMusketeers;

import java.util.Arrays;
import java.util.List;

public class ProximityStrat implements Strategy{
	/**
     * Calculates a score for the given Board, based on musketeer positions
     * and number of possible musketeer moves. A higher score means the Musketeer
	 * player is winning, while a lower score means the Guard player is winning.
	 * 
	 * STRATEGY:
     * Gives higher score if Musketeers are further apart after a move.
     * Gives lower score if Musketeers are closer together after a move.
     * @param board Board to calculate the score of.
     * @return int Score of the given Board.
     */
    @Override
    public int evalBoard(Board board) {
        return Math.max(this.evaluateRow(board), this.evaluateCol(board));
    }
    
	/**
	 * Checks how far apart the MUSKETEERs are, column-wise.
	 * @param board Board to be evaluated.
	 * @return int score calculated.
	 */
    private int evaluateCol(Board board) {
        List<Cell> musketeers = board.getMusketeerCells();
        
        int m1Col = musketeers.get(0).getCoordinate().col;
        int m2Col = musketeers.get(1).getCoordinate().col;
        int m3Col = musketeers.get(2).getCoordinate().col;
        
        int[] cols = {m1Col, m2Col, m3Col};
        Arrays.sort(cols);
        return Math.max(cols[1] - cols[0], cols[2] - cols[1]);
    }
    
	/**
	 * Checks how far apart the MUSKETEERs are, row-wise.
	 * @param board Board to be evaluated.
	 * @return int score calculated.
	 */
    private int evaluateRow(Board board) {
        List<Cell> musketeers = board.getMusketeerCells();
        
        int m1Row = musketeers.get(0).getCoordinate().row;
        int m2Row = musketeers.get(1).getCoordinate().row;
        int m3Row = musketeers.get(2).getCoordinate().row;
        
        int[] rows = {m1Row, m2Row, m3Row};
        Arrays.sort(rows);
        return Math.max(rows[1] - rows[0], rows[2] - rows[1]);
    }
}
