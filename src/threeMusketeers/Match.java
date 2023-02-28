package threeMusketeers;

public class Match {
	private String muskPlayerName;
	private String guardPlayerName;
	private String winnerName;
	private String date;
	
	/**
     * Constructor for a new Match instance, given seperated information.
     * @param muskPlayerName String with the musketeer player's name.
     * @param guardPlayerName String with the guard player's name.
     * @param winnerName String with the winner of the match's name.
     * @param date String representation of the date of the match.
     */
	public Match(String muskPlayerName, String guardPlayerName, String winnerName, String date) {
		this.muskPlayerName = muskPlayerName;
		this.guardPlayerName = guardPlayerName;
		this.winnerName = winnerName;
		this.date = date;
	}
	
	/**
     * Constructor for a new Match instance, given information in an array.
     * @param arrayMatchInfo Array with Musketeer's name, Guard's name, winner's name, and the date.
     */
	public Match(String[] arrayMatchInfo) {
		this.muskPlayerName = arrayMatchInfo[0];
		this.guardPlayerName = arrayMatchInfo[1];
		this.winnerName = arrayMatchInfo[2];
		this.date = arrayMatchInfo[3];
	}
	
    /**
     * Getter for all Match attributes, in an Array format.
     * @return Array with Musketeer's name, Guard's name, winner's name, and the date.
     */
	public String[] getMatchInfo() {
		String[] matchInfo = {muskPlayerName, guardPlayerName, winnerName, date};
		return matchInfo;
	}
	
    /**
     * Getter for the Musketeer player's name.
     * @return muskPlayerName
     */
	public String getMusketeerPlayer() {
		return muskPlayerName;
	}
	
    /**
     * Getter for the Guard player's name.
     * @return guardPlayerName
     */
	public String getGuardPlayer() {
		return guardPlayerName;
	}
	
    /**
     * Getter for the match winner's name.
     * @return winnerName
     */
	public String getWinner() {
		return winnerName;
	}

	/**
     * Getter for the date of the game.
     * @return date
     */
	public String date() {
		return date;
	}
	
	@Override
	public String toString() {
		
		return muskPlayerName + "," + guardPlayerName + "," + winnerName + "," + date;
		
	}
	
}
