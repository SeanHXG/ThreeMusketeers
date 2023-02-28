package threeMusketeers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

//COMMAND PATTERN: RECEIVER
public class PlayerRecords {
	private HashMap<String, Player> playersRegistry = new HashMap<>();
	
	/**
	 * Constructor for a new PlayerRecords.
	 */
	public PlayerRecords() {
		loadPlayers();
	}
	
	/**
	 * Attempts to add selectedPlayer to the playersRegistry.
	 * @param selectedPlayer Player to be added.
	 * @return true if the player is added to playersRegistry, false otherwise.
	 */
	public Boolean addPlayer(Player selectedPlayer) {
		if (playersRegistry.containsKey(selectedPlayer.getName())) {
			System.out.println("***Username already taken!***");
			return false;
		} else {
			playersRegistry.put(selectedPlayer.getName(), selectedPlayer);
			System.out.println("***Added new player '" + selectedPlayer.getName() + "' to registry.***");
			savePlayers();
			return true;
		}
	}
	
	/**
	 * Attempts to remove selectedPlayer from playersRegistry
	 * @param selectedPlayer Player to be removed
	 * @return true if the player is removed from playersRegistry, false otherwise.
	 */
	public Boolean removePlayer(Player selectedPlayer) {
		if (selectedPlayer != null && playersRegistry.containsKey(selectedPlayer.getName())) {
			if (selectedPlayer.getName().equals("Player1") || selectedPlayer.getName().equals("Player2")) {
				System.out.println("***You can't remove this default player!***");
				return false;
			}
			playersRegistry.remove(selectedPlayer.getName());
			System.out.println("***Removed player '" + selectedPlayer.getName() + "' from registry.***");
			savePlayers();
			return true;
		} else {
			System.out.println("***That player does not exist!***");
			return false;
		}
	}
	
	/**
	 * Loads all registered players from "./players" into playersRegistry.
	 * player files are named 'p_[playername].txt', formatted as:
	 * Line 1: [playername]
	 * Lines 2, 3, ...: [musketeerPlayer],[guardPlayer],[winner],[date]
	 * where lines 2, 3, ... represent matches played.
	 */
	private void loadPlayers() {
		File playersFolder = new File("./players");
		
		try {
			for (File playerFile : playersFolder.listFiles()) {
				if (playerFile.getName().substring(0, 2) == "p_") { // Check valid player file
					continue;
				} else {
					Scanner reader = new Scanner(playerFile);
					String playerName = reader.nextLine();
					ArrayList<Match> matches = new ArrayList<>();
				    while (reader.hasNextLine()) {
				    	String[] matchInfo = reader.nextLine().split(",");
				    	matches.add(new Match(matchInfo));
				    }
				    playersRegistry.put(playerName, new Player(playerName, matches));
				}
			}  
			System.out.println("***Loaded " + playersFolder.listFiles().length + " player files.***");
		} catch (FileNotFoundException e) {
			System.out.println("***Load registered players: Folder '/players' was not found.***");
		}
	}
	
	/**
	 * Saves all registered players and their match history to ./players/p_[playername].txt.
	 */
	public void savePlayers() {
		try {
			for (Player player : playersRegistry.values()) {
				File playerFile = new File("./players/p_" + player.getName() + ".txt");
				FileWriter playerWriter = new FileWriter(playerFile.getAbsoluteFile());
				
				playerWriter.write(player.getName() + "\n");
				for (Match match : player.getMatchHist()) {
					playerWriter.write(match.toString() + "\n");
				}
				
				System.out.println(player.getName() + " saved to" + playerFile.getName());
				
				playerWriter.close();
			}
			System.out.println("***Saved " + playersRegistry.size() + " player files.***");
			removeUnregistered();
		} catch (IOException e) {
			System.out.println("***Save registered players: An error occurred while saving.***");
		}
	}
	
	/**
	 * Deletes files for unregistered players.
	 */
	private void removeUnregistered() {
		File[] existingFiles = new File("./players").listFiles();
		ArrayList<String> registeredPlayers = new ArrayList<>();
		for (Player player : playersRegistry.values()) {
			registeredPlayers.add("p_" + player.getName() + ".txt");
		}
		for (File file : existingFiles) {
			if (!registeredPlayers.contains(file.getName())) {
				file.delete();
			}
		}
	}
	
	/**
	 * Getter for a HashMap of registered players.
	 * @return playersRegistry
	 */
	public HashMap<String, Player> getRegisteredPlayers() {
		return playersRegistry;
	}
}
