package threeMusketeers;

import java.util.List;

// Helps generate best move on board based on heuristics and possible moves
public class MoveGenerator {
	
    /**
     * Generate a new move, based on the given parameters.
     * @param boardCopy Board copy of the current game board.
     * @param depth int representing how many steps ahead to check.
     * @param strat Strategy to follow.
     * @return A pair containing a Move (the best calculated move) and an int (its score).
     */
	public Object[] generate(Board boardCopy, int depth, Strategy strat) {
        int bestScore = boardCopy.getTurn().equals(Piece.Type.MUSKETEER) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Move chosenMove = null;
        Object[] moveScorePair = new Object[2];

        List<Move> possibleMoves = boardCopy.getPossibleMoves();
        System.out.println("Moves: " + possibleMoves);
        for (Move move: possibleMoves) {
            Move moveCopy = new Move(move);
            Piece.Type turn = boardCopy.getTurn();
            boardCopy.move(move);

            int score = this.minimax(depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, boardCopy, strat);

            System.out.printf("Move: %s Score: %d\n", move, score);
            if (turn.equals(Piece.Type.MUSKETEER) && score > bestScore) {
                bestScore = score;
                chosenMove = new Move(moveCopy);
            }
            else if (turn.equals(Piece.Type.GUARD) && score < bestScore) {
                bestScore = score;
                chosenMove = new Move(moveCopy);
            }

            boardCopy.undoMove(moveCopy);
        }

        assert chosenMove != null;
        moveScorePair[0] = chosenMove;
        moveScorePair[1] = bestScore;
        return moveScorePair;
	}
	
    /**
     * Generate a new move based on the minimax algorithm with alpha-beta pruning.
     * @param depth int representing how many steps ahead to check.
     * @param alpha the alpha value for alpha-beta pruning.
     * @param beta the beta value for alpha-beta pruning.
     * @param boardCopy Board copy of the current game board.
     * @param strat Strategy to follow.
     * @return
     */
	private int minimax(int depth, int alpha, int beta, Board boardCopy, Strategy strat) {
        // Base case
        if (depth == 0 || boardCopy.isGameOver()) {
            return strat.evalBoard(boardCopy);
        }

        int bestScore = boardCopy.getTurn().equals(Piece.Type.MUSKETEER) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        List<Move> possibleMoves = boardCopy.getPossibleMoves();
        for (Move move: possibleMoves) {
            Move moveCopy = new Move(move);
            Piece.Type turn = boardCopy.getTurn();
            boardCopy.move(move);

            int score = this.minimax(depth - 1, alpha, beta, boardCopy, strat);

            if (turn.equals(Piece.Type.MUSKETEER)) {
                bestScore = Math.max(bestScore, score);
                alpha = Math.max(alpha, bestScore);
            }
            else {
                bestScore = Math.min(bestScore, score);
                beta = Math.min(beta, bestScore);
            }

            boardCopy.undoMove(moveCopy);

            if (beta <= alpha) {
                break;
            }
        }

        return strat.evalBoard(boardCopy);
    }
}
