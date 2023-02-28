package threeMusketeers;

public abstract class BoardBuilder {
	/*
	 * Note that while this class shares a similar name with BoardCreator, a 
	 * BoardBuilder instance handles the actual creation of a Board object, while
	 * a BoardCreator instance handles user input for the creation of the board.
	 */


	private Board board;
	private int numMusketeers;

	/**
	 * Constructor for a new BoardBuilder, with an existing template.
	 * @param filePath the filepath for the template of the Board to be built.
	 */
	public BoardBuilder(String filePath) {
		this.board = new Board(filePath);
		this.numMusketeers = 0;
	}
	
	/**
	 * Constructor for a new BoardBuilder, without an existing template.
	 * The resulting board will be rectangular, with dimensions 5 x size.
	 * The bottom (5 - size) rows will be filled with blockers.
	 * @param size the height of the board.
	 */
	public BoardBuilder(int size) {
		
		Cell[][] board = new Cell[5][5];
		
		// Fill the board with empty cells.
		for (int row = 0; row < 5; row++) {
			for (int col = 0; col < 5; col++) {
				board[row][col] = new Cell(new Coordinate(row, col));
			}
		}
		
		// Set the bottom (5 - size) rows as blockers.
		for (int n = 5; n > size; n--) {
			for (Cell cell: board[n - 1]) {
				cell.setPiece(new Blocker());
			}
		}
		
		this.board = new Board(board);
		this.numMusketeers = 0;
	}
	
	/**
	 * Attempts to set the Piece attribute of the Cell cell to be piece.
	 * If the user attempts to set a Musketeer piece while there are already
	 * 3 Musketeer pieces on the board, this will fail and display an error message.
	 * If the user attempts to set a Musketeer piece otherwise, also increments the
	 * musketeer count by one.
	 * @param piece a Piece to be set
	 * @param cell a Cell to have piece set on
	 */
	public void addPiece(Piece piece, Cell cell) {
		if (piece.getType().getType().equals("MUSKETEER")) {
			if (this.numMusketeers < 3) {
				cell.setPiece(piece);
				this.numMusketeers++;
			}
			else {
				System.out.println("It's called the THREE musketeers, not the FOUR musketeers!");
			}
		}
		
		else {
			cell.setPiece(piece);
		}
	}

	/**
	 * Removes a piece from a Cell. Decrements the musketeer count by one,
	 * if there was a musketeer on that cell.
	 * @param cell a Cell to have its piece removed.
	 */
	public void removePiece(Cell cell) {
		if (cell.getPiece().getType().getType().equals("MUSKETEER")) {
			this.numMusketeers--;
		}

		cell.setPiece(null);
	}

	/**
	 * Fills the remaining empty Cells on the Board with Guard pieces.
	 */
	public void fill() {
		for (Cell[] row: board.board) {
			for (Cell cell: row) {
				if (cell.getPiece() == null) {
					cell.setPiece(new Guard());
				}
			}
		}
	}

	/**
	 * Saves the current Board being built with the saveBoard() function.
	 * @param title the title of the board to be saved
	 */
	public void finishBoard(String title) {
		board.saveBoard(title);
	}

	/**
	 * Getter for Board. 
	 * @return board
	 */
	public Board getBoard() {
		return this.board;
	}
	
	/**
	 * Getter for the number of musketeers currently on the board.
	 * @return numMusketeers
	 */
	public int getNumMusketeers() {
		return this.numMusketeers;
	}
	
	public String toString() {
        return board.toString();
    }
}