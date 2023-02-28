package threeMusketeers;

//COMMAND PATTERN: CONCRETE COMMAND 
public class AddPlayerOrder implements Command {
	PlayerRecords records;
	Player selectedPlayer;
	
 /**
  * Create a new AddPlayerOrder instance, with the given parameters
  * @param records a PlayerRecords instance
  * @param selectedPlayer a Player instance, to be added to records
  */
	public AddPlayerOrder(PlayerRecords records, Player selectedPlayer) {
		this.records = records;
		this.selectedPlayer = selectedPlayer;
	}

 /**
  * Adds selectedPlayer to records.
  * @return records with selectedPlayer added.
  */
	public Boolean execute() {
		return records.addPlayer(selectedPlayer);
	}
	
}