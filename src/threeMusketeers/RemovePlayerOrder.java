package threeMusketeers;

//COMMAND PATTERN: CONCRETE COMMAND
public class RemovePlayerOrder implements Command {
	PlayerRecords records;
	Player selectedPlayer;
	
	
 /**
  * Create a new RemovePlayerOrder instance, with the given parameters
  * @param records a PlayerRecords instance
  * @param selectedPlayer a Player instance, to be removed from records
  */
	public RemovePlayerOrder(PlayerRecords records, Player selectedPlayer) {
		this.records = records;
		this.selectedPlayer = selectedPlayer;
	}
	
 /**
  * Removes selectedPlayer to records.
  * @return records with selectedPlayer removed.
  */
	public Boolean execute() {
		return records.removePlayer(selectedPlayer); 
	}

}