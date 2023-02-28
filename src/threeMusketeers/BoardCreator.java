package threeMusketeers;

import java.util.Scanner;

import Exceptions.InvalidMoveException;

public class BoardCreator {
	/*
	 * Note that while this class shares a similar name with BoardBuilder, a 
	 * BoardBuilder instance handles the actual creation of a Board object, while
	 * a BoardCreator instance handles user input for the creation of the board.
	 */
	
    private final Scanner scanner = new Scanner(System.in);
    private BoardBuilder builder;
    private Board board;
    private Boolean building = true;
	
	/**
	 * Constructor for a BoardCreator.
	 */
    public BoardCreator() {
    }
    
	/**
	 * Handles user input and utilizes a BoardBuilder to create a new board.
	 */
    public void buildBoard() {
    	createBuilder();
    	this.board = builder.getBoard();
    	String action;
		// Start building the board.
    	while (building) {
    		System.out.println(board.toString());
    		action = this.getActionInput();
    		Cell cell;
    		switch (action) {
        	case("A"):
        		cell = this.getCellInput("a");
        		String type = this.getPieceInput("add");
        		if (type.equals("M")) {
        			builder.addPiece(new Musketeer(), cell);
        		}
        		else {
        			builder.addPiece(new Guard(), cell);
        		}
        		break;
        	case("R"):
        		cell = this.getCellInput("r");
        		builder.removePiece(cell);
        		break;
        	case("F"):
        		builder.fill();
        		break;
        	case("S"):
        		if (builder.getNumMusketeers() == 3) {
        			building = false;
        		}
        		else {
        			System.out.println("There must be exactly 3 MUSKETEERs, that's the name of the game!");
        		}
    			break;
    		}
    	}
		if (this.getPieceInput("finish").equals("G")) {
			board.changeTurn();
		}
		// Finished building the board, begin saving process.
		System.out.print("Enter the name of the board: ");
        String temp = "";
        while(true){
                temp = scanner.next();
                temp = temp.toLowerCase();
                if (temp.equals("circletemplate") || temp.equals("triangletemplate") || temp.equals("default")) {
                    System.out.print("This name would break our system. Please choose a different one: ");
                    continue;
                }
            
            break;
        }
        builder.finishBoard(temp);
    }
    
	/** 
	 * Sets the current BoardBuilder builder to a template based on
	 * user input.
	 */
    private void createBuilder() {
    	String buildType = createBuilderInput();
    	switch (buildType) {
    	case("R"):	    	
    		System.out.print("Enter an integer between 1 and 5 for the height: ");
    		while (!scanner.hasNext("[12345]")) {
	             System.out.print("Invalid option. Enter an integer between 1 and 5 for the height: ");
	             scanner.next();
	    	}
	    	this.builder = new RectangularBoardBuilder(scanner.nextInt());
	    	break;
    	case("T"):
    		this.builder = new TriangularBoardBuilder();
    		break;
    	case("C"):
    		this.builder = new CircularBoardBuilder();
    		break;
    	}
    }
    
	/**
	 * Prompts the user to select the type of board template they would like to
	 * begin building with.
	 * @return String containing the user's response.
	 */
	private String createBuilderInput() {
        System.out.println("Enter the type of board to be made");
        System.out.print("'R' for rectangular, 'T' for triangular, or 'C' for circular: ");
        while (!scanner.hasNext("[RTCrtc]")) {
            System.out.print("Invalid option. Enter 'R', 'T', or 'C': ");
            scanner.next();
        }
        return scanner.next().toUpperCase();
    }
	
	/**
	 * Prompts the user to select a board building action.
	 * @return String containing the user's response.
	 */
	private String getActionInput() {
		System.out.print("Enter 'A' to add a piece, 'R' to remove a piece, 'F' to fill empty cells with GUARDS, or 'S' to finish building the board:");
        while (!scanner.hasNext("[ARFSarfs]")) {
            System.out.print("Invalid option. Enter 'A', 'R', 'F' or 'S': ");
            scanner.next();
        }
        return scanner.next().toUpperCase();
	}
	
	/**
	 * Prompts the user to select a cell in order to perform an action
	 * onto.
	 * @param type String representation of an action. "a": add, "r": remove.
	 * @return the Cell on the board that the user has selected.
	 */
	private Cell getCellInput(String type) {
		Coordinate coord = null;
		String coordString = "";
        while (true) {
			// Prompt the user for a Coordinate to be modified
			System.out.print("Enter the coordinate of the cell to be modified: ");
			if (scanner.hasNext()) {
				coordString = scanner.next();
			}
	        coord = null;
	        
			// Attempt to set coord to be the Coordinate represented by coordString
	        try {
	        	coord = Utils.parseUserMove(coordString);
	        } catch (InvalidMoveException e) {
	            System.out.println("Please enter a valid coordinate (ex. A1).");
	            continue;
	        }

	        if (!(board.onBoard(coord.row, coord.col))) {
	            System.out.println("Please enter a coordinate on the board.");
	            continue;
	        }
	        if (type.equals("a") && board.getCell(coord).getPiece() != null) {
	        	System.out.println("There's already a piece here!");
	        	continue;
	        }
	        if (type.equals("r") && board.getCell(coord).getPiece() != null && board.getCell(coord).getPiece().getType().toString() == "BLOCKER") {
	        	System.out.println("You can't remove blockers!");
	        	continue;
	        }
	        return this.board.getCell(coord);
		}
	}
	
	/**
	 * Prompts the user to select a piece, for piece-related actions.
	 * Different prompts will be displayed based on the String variant.
	 * @param variant String dictating the desired prompt.
	 * @return String representing the user's response.
	 */
	private String getPieceInput(String variant) {
		if (variant.equals("add")){
			System.out.print("Enter 'M' to add a MUSKETEER or 'G' add a GUARD: ");
		}
		else {
			System.out.println("Enter the starting piece type.");
			System.out.print("'M' for MUSKETEER, or 'G' for GUARD: ");
		}
        while (!scanner.hasNext("[MGmg]")) {
        	System.out.println();
            System.out.print("Invalid option. Enter 'M' or 'G': ");
            scanner.next();
        }
        return scanner.next().toUpperCase();
	}
}

