package com.numbercortex;

import java.util.ArrayList;

public class Local implements Exchangeable {
	
	private ArrayList<Player> players = new ArrayList<Player>();

	public void register(Player player) {
		players.add(player);
	}

	@Override
	public void update (CortexState state) {
		for (Player player : players) {
			if (player.getName().equals(state.getCurrentPlayer())) {
				player.updateState(state);
			}
		}
	}
	
	public ArrayList<String> getRegisteredPlayerNames() {
		ArrayList<String> playerNames = new ArrayList<String>();
		for (Player player : players) {
			String playerName = player.getName();
			playerNames.add(playerName);
		}
		return playerNames;
	}

}
