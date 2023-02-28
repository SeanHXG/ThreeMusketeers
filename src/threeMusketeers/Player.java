package threeMusketeers;

import java.util.ArrayList;
import java.util.List;

public class Player extends Observer{
	private String name;
	private ArrayList<Match> matchHistory;
	private int numWins;
	private int playerRank;
	
	/**
	 * Constructor for players without past matches.
	 * @param name String containing the name of the player.
	 */
	public Player(String name) {
		this.name = name;
		this.numWins = 0;
		this.playerRank = -1;
		matchHistory = new ArrayList<>();
	}
	
	/**
	 * Constructor for players with past matches, primarily used for undoing
	 * a delete command.
	 * @param name String containing the name of the player.
	 * @param matchHistory An Array of the player's past matches.
	 */
	public Player(String name, ArrayList<Match> matchHistory) {
		this.name = name;
		this.numWins = 0;
		this.playerRank = -1;
		this.matchHistory = matchHistory;
		this.loadWins();
	}
	
	/**
	 * Sets the player's numWins to the number of wins found in
	 * their match history.
	 */
	private void loadWins() {
		numWins = 0;
		if (this.matchHistory != null) {
			for (Match match : this.matchHistory) {
				if (match.getWinner().equals(this.name)){
					this.numWins++;
				}
			}
		}
	}
	
	/**
	 * Adds a win to the player's records.
	 */
	public void addWin() {
		this.numWins++;
	}
	
	/**
	 * Adds a match to the player's records.
	 * @param match Match to be added.
	 */
	public void addMatch(Match match) {
		matchHistory.add(match);
	}
	
	/**
	 * Getter for the player's wins.
	 * @return numWins
	 */
	public int getWins() {
		return this.numWins;
	}
	
	/**
	 * Getter for the player's name.
	 * @return name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Getter for the player's match history.
	 * @return matchHistory
	 */
	public ArrayList<Match> getMatchHist(){
		return matchHistory;
	}

	/**
	 * Getter for the player's rank.
	 * @return playerRank
	 */
	public int getRank() {
		return this.playerRank;
	}
	
	@Override
	public String toString() {
		StringBuilder playerStr = new StringBuilder("PlayerName: " + name + "\n" + "Number of wins: " + this.numWins + "\n" + "Rank: " + this.playerRank + "\n");
		playerStr.append("Musketeer|Guard|Winner|Date\n");
		for (Match match : matchHistory) {
			String matchStr = match.toString();
			matchStr = matchStr.replaceAll(",", "|");
			playerStr.append(matchStr + "\n");
		}
		return playerStr.toString();
	}

	@Override
	public void update(List<Player> ranks) {
		this.playerRank = ranks.indexOf(this) + 1;
		if (this.playerRank == 1) {
			System.out.println("--------------------------");
			System.out.println(this.name + " is currently the champ.");
		}
	}
}
