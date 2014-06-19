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
		for (Player player : players) {
			if (player.getName().equals(state.getCurrentPlayer())) {
				player.updateState(state);
			}
		}
	}
	
	@Override
	public void placeNumber(int number, int coordinate) {
		model.placeNumber(currentPlayer, number, coordinate);
	}
	
	@Override
	public void chooseNumber(int nextNumber) {
		model.chooseNumber(currentPlayer, nextNumber);
	}

}
