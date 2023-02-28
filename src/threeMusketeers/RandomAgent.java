package threeMusketeers;

import java.util.Random;

public class RandomAgent extends Agent {

    /**
     * Create a new RandomAgent for board.
     * @param board Board the RandomAgent is assigned to.
     */
    public RandomAgent(Board board) {
        super(board);
    }

    /**
     * Gets a valid random move the RandomAgent can do.
     * @return a valid Move that the RandomAgent can perform on the Board
     */
    @Override
    public Move getMove() { 
    	Random random = new Random();
        return board.getPossibleMoves().get(random.nextInt(board.getPossibleMoves().size()));
    }
}