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

	private ArrayList<ModelListener> listeners = new ArrayList<ModelListener>();
	private ArrayList<String> players = new ArrayList<String>();

	private static final int BOARD_SIZE = 16;
	
	private String winner; // Optional
	private int[] winningCoordinates; // Optional
	
	public static class Singleton {
		private static CortexModel INSTANCE = new DefaultCortexModel();
	}
	private DefaultCortexModel() {}
	public static CortexModel getInstance() {
		return Singleton.INSTANCE;
	}

	@Override
	public void register(ModelListener listener) {
		listeners.add(listener);
		players.add(listener.getName());
		if (listeners.size() == 2) {
			startGame();
		}
	}

	private void startGame() {
		// clearVariables();
		setInitialBoardState();
		setInitialAvailableNumbers();
		setFirstPlayer();
		coordinateNumberMap.put(3, 3);
		message = currentPlayer + " starts the game!";
		CortexState state = new CortexState.CortexStateBuilder(message, currentPlayer, players, chosenNumber, coordinateNumberMap, availableNumbers).build();
		for (ModelListener listener: listeners) {
			listener.update(state);
		}
	}

	private void setFirstPlayer() {
		// Math.random();
		currentPlayer = players.get(0);
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

	@Override
	public void placeNumber(int number, int coordinate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void chooseNumber(int nextNumber) {
		// TODO Auto-generated method stub
		
	}

}
