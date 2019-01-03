package com.uofr.course.csc442.hw.hw1.tictactoe.model;
/**
 * 
 * Enum for the players X and O of the game.
 * We default X to be the ,aximizer always
 * just to avoid unnecessary logic
 * @author tusharkumar
 *
 */
public enum Player {

	X("x", true),
	O("o", false);
	
	private String playerName;
	
	//Is the player maximizer for the game
	private boolean max;
	
	private Player(String playerName, boolean max) {		
		this.playerName = playerName.toLowerCase();
		this.max = max;
	}

	public String getPlayerName() {
		return playerName;
	}
	
	public boolean isMax() {
		return max;
	}
	public String toString() {
		return playerName;
	}
	
	/**
	 * Get a player given who is the other player
	 * @return
	 */
	public Player getOtherPlayer() {
		for(Player player : Player.values()) {
			if(!player.playerName.equalsIgnoreCase(this.playerName)) {
				return player;
			}
		}
		return null;
	}

	/**
	 * Get player from input choice of player
	 * @param playerChoice
	 * @return
	 */
	public static Player playerFromChoice(String playerChoice) {
		for(Player playerName : Player.values()) {
			if(playerName.playerName.equalsIgnoreCase(playerChoice)) {
				return playerName;
			}
		}
		return null;
	}
	
}
