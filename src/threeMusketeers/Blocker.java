package threeMusketeers;

public class Blocker extends Piece {

    public Blocker() {
        super("=", Type.BLOCKER);
    }

    /**
     * Always returns false because blockers cannot move.
     * @param cell Cell to check if the Blocker can move onto
     * @return false.
     */
    @Override
    public boolean canMoveOnto(Cell cell) { 
        return false;
    }
}
