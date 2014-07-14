package com.numbercortex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CortexState implements Serializable {

	public static class CortexStateBuilder {
		private final String message;
		private final ArrayList<String> players;
		private final String currentPlayer;
		private final int chosenNumber;
		private final Map<Integer, Integer> coordinateNumberMap;
		private final ArrayList<Integer> availableNumbers;

		private String winner; // Optional
		private String winningAttribute; // Optional
		private int[] winningValues; // Optional

		public CortexStateBuilder(String message, String currentPlayer, ArrayList<String> players, int chosenNumber,
				Map<Integer, Integer> coordinateNumberMap, ArrayList<Integer> availableNumbers) {
			this.message = message;
			this.currentPlayer = currentPlayer;
			this.players = players;
			this.chosenNumber = chosenNumber;
			this.coordinateNumberMap = coordinateNumberMap;
			this.availableNumbers = availableNumbers;
		}

		public CortexStateBuilder win(String winner, String winningAttribute, int[] winningValues) {
			this.winner = winner;
			this.winningAttribute = winningAttribute;
			this.winningValues = winningValues;
			return this;
		}

		public CortexState build() {
			return new CortexState(this);
		}

	}
	
	private String message;
	private ArrayList<String> players;
	private String currentPlayer;
	private int chosenNumber;
	private Map<Integer, Integer> coordinateNumberMap;
	private ArrayList<Integer> availableNumbers;

	private String winner; // Optional
	private String winningAttribute; // Optional
	private int[] winningValues; // Optional

	private CortexState(CortexStateBuilder builder) {
		this.message = builder.message;
		this.players = builder.players;
		this.currentPlayer = builder.currentPlayer;
		this.chosenNumber = builder.chosenNumber;
		this.coordinateNumberMap = builder.coordinateNumberMap;
		this.availableNumbers = builder.availableNumbers;
		this.winner = builder.winner;
		this.winningAttribute = builder.winningAttribute;
		this.winningValues = builder.winningValues;
	}

	public ArrayList<Integer> getAvailableNumbers() {
		return (ArrayList<Integer>) availableNumbers.clone();
	}

	public int getChosenNumber() {
		return chosenNumber;
	}

	public Map<Integer, Integer> getCoordinateNumberMap() {
		return (Map<Integer, Integer>) new HashMap<Integer, Integer>(coordinateNumberMap).clone();
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public String getMessage() {
		return message;
	}

	public ArrayList<String> getPlayers() {
		return (ArrayList<String>) players.clone();
	}

	public String getWinner() {
		return winner;
	}

	public String getWinningAttribute() {
		return winningAttribute;
	}

	public int[] getWinningValues() {
		if (winningValues == null) {
			return null;
		} else {
			return winningValues.clone();
		}
	}

}
