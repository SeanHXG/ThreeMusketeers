package threeMusketeers;

import java.util.ArrayList;

// COMMAND PATTERN: INVOKER
public class GameManager {	
    // Stack to track commands for undoing.
	public ArrayList<Command> changes = new ArrayList<>();

	/**
     * Recieves a command to add or remove a player, and attempts to executes it.
     * @param cmd Command to add or remove a player.
     */
	public void modifyPlayerRegistry(Command cmd) {
		Boolean isSuccessful = cmd.execute();
		if (isSuccessful) {
			changes.add(cmd);
		}
	}
	
    /**
     * Attempts to undo the previous command, utilizing the Stack changes.
     */
	public void undo() {
		if (changes.size() == 0) {
			System.out.println("***No commands to undo!***");
		} 
        else {
            Command prevCMD = changes.remove(changes.size() - 1);
            Command toUndo;
            if (prevCMD instanceof AddPlayerOrder) {
                toUndo = new RemovePlayerOrder(((AddPlayerOrder) prevCMD).records, ((AddPlayerOrder) prevCMD).selectedPlayer);
                toUndo.execute();
                System.out.println("***Undoing previous add player command...***");
            } 
            else if (prevCMD instanceof RemovePlayerOrder) {
                toUndo = new AddPlayerOrder(((RemovePlayerOrder) prevCMD).records, ((RemovePlayerOrder) prevCMD).selectedPlayer);
                toUndo.execute();
                System.out.println("***Undoing previous remove player command...***");
            }
		}
	}
}
