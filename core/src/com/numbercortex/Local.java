package com.numbercortex;

import java.util.ArrayList;

public class Local implements Exchangeable {
	
	private ArrayList<Player> players = new ArrayList<Player>();
	private CortexModel model = new DefaultCortexModel(this);
	private String currentPlayer;

	public Local() {}

	@Override
	public void chooseNumber(String playerName, int nextNumber) {
		model.chooseNumber(currentPlayer, nextNumber);
	}

	@Override
	public void placeNumber(String playerName, int coordinate, int number) {
		model.placeNumber(currentPlayer, coordinate, number);
	}
	
	@Override
	public void register(Player... players) {
		for (int i = 0; i < players.length; i++) {
			this.players.add(players[i]);
			model.register(players[i].getName());
		}
	}
	
	@Override
	public void updateState (CortexState state) {
		currentPlayer = state.getCurrentPlayer();
		for (Player player : players) {
			if (player.getName().equals(state.getCurrentPlayer())) {
				player.getScreen().updateState(state);
			}
		}
	}

}
