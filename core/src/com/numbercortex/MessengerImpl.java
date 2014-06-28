package com.numbercortex;

import java.util.ArrayList;

public class MessengerImpl implements Messenger {

	private ArrayList<Player> players = new ArrayList<Player>();
	private String currentPlayer;
	private CortexModel model;

	private MessengerImpl() {}

	public static MessengerImpl createMessenger() {
		MessengerImpl local = new MessengerImpl();
		local.model = new DefaultCortexModel(local);
		return local;
	}

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
	public void updateState(CortexState state) {
		currentPlayer = state.getCurrentPlayer();
		for (Player player : players) {
			if (player.getName().equals(state.getCurrentPlayer())) {
				player.updateState(state);
			}
		}
	}

}
