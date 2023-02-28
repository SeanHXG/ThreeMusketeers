package threeMusketeers;

import java.util.HashMap;
import java.util.Scanner;

import Exceptions.InvalidMoveException;

public class HumanAgent extends Agent {

	HashMap<String, Strategy> possibleStrats = new HashMap<>();

    /**
     * Create a new HumanAgent for board.
     * @param board Board the HumanAgent is assigned to.
     */
    public HumanAgent(Board board) {
        super(board);
        possibleStrats.put("Greedy", new GreedyStrat());
        possibleStrats.put("PseudoIntellectual", new PseudoIntellectualStrat());
        possibleStrats.put("Proximity", new ProximityStrat());
    }
    
    /**
     * Getter for possible strategies
     * @return possibleStrats
     */
    public HashMap<String, Strategy> getStrats(){
    	return possibleStrats;
    }
    
    /**
     * Displays a hint move of a given strategy
     * @param hintStratName String name of a hint strategy.
     */
    public void getHint(String hintStratName) {
    	Board boardCopy = new Board(board);
    	Object[] pairMoveScore = new MoveGenerator().generate(boardCopy, 10, possibleStrats.get(hintStratName));
    	Move chosenMove = (Move) pairMoveScore[0];
    	int bestScore = (int) pairMoveScore[1];
    	
    	assert chosenMove != null;
        System.out.printf("[%s (" + hintStratName + ")] Moving piece %s to %s. Best Score: %d\n",
        		board.getTurn().getType(), chosenMove.fromCell.getCoordinate(), chosenMove.toCell.getCoordinate(),
                bestScore);
    }

    /**
     * Prompts the user to input "to" and "from" coordinates for a move, then
     * ensures it's valid. After, creates a Move object with the chosen fromCell and toCell
     * @return the valid human inputed Move
     */
    @Override
    public Move getMove() { 
        Scanner input = new Scanner(System.in);
        Boolean inputLoop = true;
        Cell fromCell = null;
        Cell toCell = null;
        Move move = null;
        
        while (inputLoop) { // Loop for fromcell
            System.out.print("Enter the coordinate of the piece to be moved: ");
            String from = input.nextLine();
            
            Coordinate fromCoord = null;
            try {
                fromCoord = Utils.parseUserMove(from);
            } catch (InvalidMoveException e) {
                System.out.println("Please enter a valid coordinate (ex. A1).");
                continue;
            }
            if (!(board.onBoard(fromCoord.row, fromCoord.col))) {
                System.out.println("Please enter a coordinate on the board.");
                continue;
            }
            
            fromCell = board.getCell(fromCoord);
            
            if (fromCell.getPiece() == null) {
                System.out.println("There's no piece here!");
                continue;
            }
            if (fromCell.getPiece().getType() != board.getTurn()) {
                System.out.println("Please select a " + board.getTurn().getType() + "!");
                continue;
            }
            
            break;
        }
        while (inputLoop) { // Loop for tocell
            System.out.print("Enter the coordinate where the piece will go: ");
            String to = input.nextLine();
            
            Coordinate toCoord = null;
            try {
                toCoord = Utils.parseUserMove(to);
            } catch (InvalidMoveException e) {
                System.out.println("Please enter a valid coordinate (ex. A1).");
                continue;
            }
            if (!(board.onBoard(toCoord.row, toCoord.col))) {
                System.out.println("Please enter a coordinate on the board.");
                continue;
            }
            
            toCell = board.getCell(toCoord);            
            move = new Move(fromCell, toCell);
            if (!board.isValidMove(move)) {
                System.out.println("Please select a valid destination (invalid)");
                if (board.getPossibleDestinations(fromCell).size() == 0) {
                    getMove();
                }
                continue;
            }
            break;
        }
        return move;
    }
}
