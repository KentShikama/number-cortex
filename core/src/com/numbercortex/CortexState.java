package com.numbercortex;

import java.io.Serializable;
import java.util.ArrayList;
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
		private int[] winningCoordinates; // Optional

		public CortexStateBuilder(Map<Integer, Integer> coordinateNumberMap, ArrayList<Integer> availableNumbers) {
			this(null, null, null, 0, coordinateNumberMap, availableNumbers);
		}

		public CortexStateBuilder(String message, String currentPlayer, ArrayList<String> players, int chosenNumber,
				Map<Integer, Integer> coordinateNumberMap, ArrayList<Integer> availableNumbers) {
			this.message = message;
			this.currentPlayer = currentPlayer;
			this.players = players;
			this.chosenNumber = chosenNumber;
			this.coordinateNumberMap = coordinateNumberMap;
			this.availableNumbers = availableNumbers;
		}

		public CortexStateBuilder win(String winner, int[] winningCoordinates) {
			this.winner = winner;
			this.winningCoordinates = winningCoordinates;
			return this;
		}

		public CortexState build() {
			return new CortexState(this);
		}

	}

	private static final long serialVersionUID = 1L;
	private String message;
	private ArrayList<String> players;
	private String currentPlayer;
	private int chosenNumber;
	private Map<Integer, Integer> coordinateNumberMap;
	private ArrayList<Integer> availableNumbers;
	private String winner; // Optional

	private int[] winningCoordinates; // Optional

	private CortexState(CortexStateBuilder builder) {
		this.message = builder.message;
		this.players = builder.players;
		this.currentPlayer = builder.currentPlayer;
		this.chosenNumber = builder.chosenNumber;
		this.coordinateNumberMap = builder.coordinateNumberMap;
		this.availableNumbers = builder.availableNumbers;
		this.winner = builder.winner;
		this.winningCoordinates = builder.winningCoordinates;
	}

	public ArrayList<Integer> getAvailableNumbers() {
		return availableNumbers;
	}

	public int getChosenNumber() {
		return chosenNumber;
	}

	public Map<Integer, Integer> getCoordinateNumberMap() {
		return coordinateNumberMap;
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public String getMessage() {
		return message;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public String getWinner() {
		return winner;
	}

	public int[] getWinningCoordinates() {
		return winningCoordinates;
	}

}
