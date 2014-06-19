package com.numbercortex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DefaultCortexModel implements CortexModel {
	
	private String currentPlayer;
	private ArrayList<Integer> availableNumbers =  new ArrayList<Integer>();
	private Map<Integer, Integer> coordinateNumberMap = new HashMap<Integer, Integer>();
	private String message;
	private int chosenNumber = -1;

	private ArrayList<String> playerNames = new ArrayList<String>();

	private static final int BOARD_SIZE = 16;
	
	private String winner; // Optional
	private int[] winningCoordinates; // Optional
	
	private Exchangeable listener;
	
	public DefaultCortexModel(Exchangeable listener) {
		this.listener = listener;
	}

	@Override
	public void register(Player player) {
		listener.register(player);
		if (listener.getRegisteredPlayerNames().size() == 2) {
			startGame();
		}
	}

	private void startGame() {
		// clearVariables();
		setInitialBoardState();
		setInitialAvailableNumbers();
		registerPlayers();
		setFirstPlayer();
		coordinateNumberMap.put(3, 3);
		message = currentPlayer + " starts the game!";
		CortexState state = new CortexState.CortexStateBuilder(message, currentPlayer, playerNames, chosenNumber, coordinateNumberMap, availableNumbers).build();
		listener.update(state);
	}

	private void setInitialBoardState() {
		coordinateNumberMap.clear();
		for (int i = 0; i < BOARD_SIZE; i++) {
			coordinateNumberMap.put(i, -1);
		}
	}

	private void setInitialAvailableNumbers() {
		availableNumbers.clear();
		for (int i = 1; i < 18; i++) {
			if (i == 9) {
				continue;
			}
			availableNumbers.add(i);
		}
	}
	
	private void registerPlayers() {
		playerNames = listener.getRegisteredPlayerNames();
	}

	private void setFirstPlayer() {
		if (Math.random() > 0.5) {
			currentPlayer = playerNames.get(0);
		} else {
			currentPlayer = playerNames.get(1);
		}
	}

	@Override
	public void placeNumber(int number, int coordinate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void chooseNumber(int nextNumber) {
		// TODO Auto-generated method stub
		
	}

}
