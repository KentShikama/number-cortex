package com.numbercortex;

import java.util.ArrayList;

public class Local implements Exchangeable {
	
	private ArrayList<Player> players = new ArrayList<Player>();
	private CortexModel model;
	private String currentPlayer;

	public Local(CortexModel model) {
		this.model = model;
		model.setExchangeable(this);
	}

	public void register(Player player) {
		players.add(player);	
		model.register(player.getName());
	}

	@Override
	public void update (CortexState state) {
		currentPlayer = state.getCurrentPlayer();
		for (Player player : players) {
			if (player.getName().equals(state.getCurrentPlayer())) {
				player.updateState(state);
			}
		}
	}
	
	@Override
	public void placeNumber(int coordinate, int number) {
		model.placeNumber(currentPlayer, coordinate, number);
	}
	
	@Override
	public void chooseNumber(int nextNumber) {
		model.chooseNumber(currentPlayer, nextNumber);
	}

}
