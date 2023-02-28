package threeMusketeers;


public class PseudoIntellectualStrat implements Strategy {
	/**
     * Calculates a score for the given Board, based on musketeer positions
     * and number of possible musketeer moves. A higher score means the Musketeer
	 * player is winning, while a lower score means the Guard player is winning.
	 * 
	 * STRATEGY:
	 * Gives higher score if number of possible Guard moves increases after given move.
     * Gives lower score if number of possible Musketeer moves increases after given move.
     * @param board Board to calculate the score of.
     * @return int Score of the given Board.
     */
    @Override
    public int evalBoard(Board board) {
    	int guardMoves = 0;
    	int muskMoves = 0;
    	for (Cell cell : board.getPossibleCells()) {
    		switch(cell.getPiece().getType()) {
    		case MUSKETEER:
    			muskMoves += 1;
    		case GUARD:
    			guardMoves += 1;
			default:
				break;
    		}
    	}
        return guardMoves - muskMoves;
    }
}
