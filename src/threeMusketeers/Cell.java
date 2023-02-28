package threeMusketeers;

public class Cell {
    private Coordinate coordinate;
    private Piece piece;

    /**
     * Creates a cell with the given coordinate.
     * @param coordinate Coordinate of the cell on the board.
     */
    public Cell(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Create a copy of a Cell
     * @param cell a Cell to make a copy of
     */
    public Cell(Cell cell) {
        this.coordinate = cell.coordinate;
        this.piece = cell.piece;
    }

    /**
     * Getter for the Cell's coordinate.
     * @return coordinate
     */
    protected Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Checks if there is a piece on this cell.
     * @return True if there is a piece on this cell, false otherwise.
     */
    protected boolean hasPiece() {
        return piece != null;
    }

    /**
     * Getter for this Cell's piece.
     * @return piece
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Sets the cell's piece.
     * @param piece Piece to be set.
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * Removes a piece from a cell.
     */
    public void removePiece() {
        this.piece = null;
    }

    @Override
    public String toString() {
        return hasPiece() ? piece.getSymbol() : " ";
    }
}
