package threeMusketeers;

public class GreedyAgent extends Agent {
    Board boardCopy;
    GreedyStrat boardGreedyEval;
    int depth = 10;

    /**
     * Create a new GreedyAgent for board. Uses default greed values.
     * @param board Board the GreedyAgent is assigned to.
     */
    public GreedyAgent(Board board) {
        super(board);
        this.boardGreedyEval = new GreedyStrat();
    }

    /**
     * /**
     * Create a new GreedyAgent for board, with the given parameters.
     * @param board Board the GreedyAgent is assigned to.
     * @param boardGreedyEval a GreedyStrat this GreedyAgent will use.
     * @param depth the depth that the GreedyStrat will search through.
     */
    public GreedyAgent(Board board, GreedyStrat boardGreedyEval, int depth) {
        super(board);
        this.boardGreedyEval = boardGreedyEval;
        this.depth = depth;
    }

    /**
     * Gets a valid move that the GreedyAgent can do.
     * Uses a MiniMax strategy with Alpha Beta pruning
     * @return a valid Move that the GreedyAgent can perform on the Board
     */
    @Override
    public Move getMove() {
    	boardCopy = new Board(board);
    	Object[] pairMoveScore = new MoveGenerator().generate(boardCopy, depth, boardGreedyEval);
    	Move chosenMove = (Move) pairMoveScore[0];
    	int bestScore = (int) pairMoveScore[1];

        assert chosenMove != null;
        System.out.printf("[%s (Greedy Agent)] Moving piece %s to %s. Best Score: %d\n",
                board.getTurn().getType(), chosenMove.fromCell.getCoordinate(), chosenMove.toCell.getCoordinate(),
                bestScore);
        return new Move(
                board.getCell(chosenMove.fromCell.getCoordinate()),
                board.getCell(chosenMove.toCell.getCoordinate()));
    }
}
