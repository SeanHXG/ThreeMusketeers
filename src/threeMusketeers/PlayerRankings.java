package threeMusketeers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class PlayerRankings implements Observable{
	
	private List<Observer> observers;
	private List<Player> ranks;
	
    /**
     * Constructor for a new PlayerRankings object.
     */
	public PlayerRankings() {
		this.observers = new ArrayList<Observer>();
		this.ranks = new ArrayList<Player>();
		//this.loadPlayers();
	}
	
    /**
     * Adds a Player to the PlayerRankings, then updates the rankings.
     * @param player Player to be added.
     */
	public void addPlayer(Player player) {
		ranks.add(player);
		this.updateRanks();
	}
	
    /**
     * Removes a player from the PlayerRankings, if able.
     * Then, updates the rankings.
     * @param player Player to be removed.
     */
	public void removePlayer(Player player) {
		ranks.remove(player);
		this.updateRanks();
	}
	
    /**
     * Update the ranks parameter.
     */
	public void updateRanks() {
		this.sortByRank();
		this.updateObservers();
	}
	
    /**
     * Sorts the List<Player> ranks by each player's wins.
     */
	private void sortByRank() {
		HashMap<Integer, ArrayList<Player>> players = new HashMap<>();
        // Create a HashMap with a number of wins as a key, and an ArrayList of
        // Players from ranks as its value.
		for (Player p : this.ranks) {
			int wins = p.getWins();
			if (players.containsKey(wins)) {
				players.get(wins).add(p);
			}
			else {
				ArrayList<Player> player = new ArrayList<>(); 
				player.add(p);
				players.put(wins, player);
			}
		}
		
        // Sort all keys in nondescending order.
		Set<Integer> keys = players.keySet();
		ArrayList<Integer> intKeys = new ArrayList<>();
		for (Integer key : keys) {
			intKeys.add(key);
		}
		Object[] keyArray = intKeys.toArray();
		Arrays.sort(keyArray);
		
        // Create the updated rankings in an ArrayList of Players.
		ArrayList<Player> newRank = new ArrayList<>();
		for (Object key : keyArray) {
			for (Player player : players.get(key)) {
				newRank.add(player);
			}
		}
		Collections.reverse(newRank);
		this.ranks = newRank;
	}
	
    /**
     * Displays the ranking as stylized text in the console.
     */
	public void printRanking() {
		int rank = 1;
		System.out.println("---------RANKINGS---------");
		for (Player player : this.ranks) {
			System.out.println(rank + ": " + player.getName() + " wins: " + player.getWins());
			rank++;
		}
		System.out.println("--------------------------");
		
	}

	@Override
	public void addObserver(Observer observer) {
		observers.add(observer);
		
	}

	@Override
	public void removeObserver(Observer observer) {
		observers.remove(observer);
		
	}

	@Override
	public void updateObservers() {
		for (Observer observer: this.observers) {
			observer.update(ranks);
		}
		
	}

}
