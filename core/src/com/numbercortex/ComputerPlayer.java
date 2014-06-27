package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

public class ComputerPlayer implements Player {

	private String name;
	private PlayScreen screen;
	private Exchangeable messenger;
	
	private enum Difficulty {
		EASY, MEDIUM, HARD;
	}

	public ComputerPlayer(String name, PlayScreen playScreen, Exchangeable messenger) {
		this.name = name;
		this.screen = playScreen;
		this.messenger = messenger;
	}
	
	/**
	 * AI function and which calls chooseNumber or placeNumber once computing is finished
	 */
	@Override
	public void updateState(CortexState state) {
		int chosenNumber = state.getChosenNumber();
		if (chosenNumber != -1) {
			ArrayList<Integer> openCoordinates = new ArrayList<Integer>();
			for (Map.Entry<Integer, Integer> entry : state.getCoordinateNumberMap().entrySet()) {
				if (entry.getValue() == -1) {
					int openCoordinate = entry.getKey();
					openCoordinates.add(openCoordinate);
				}
			}
			int chosenCoordinatePosition = (int) (Math.random() * openCoordinates.size());
			int chosenCoordinate = openCoordinates.get(chosenCoordinatePosition);
			placeNumber(null, chosenCoordinate, chosenNumber);
		} else {
			ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
			int nextNumberPosition = (int) (Math.random() * availableNumbers.size());
			int nextNumber = availableNumbers.get(nextNumberPosition);
			chooseNumber(null, nextNumber);
			return;
		}
	}
	
	@Override
	public void chooseNumber(String player, int nextNumber) {
		messenger.chooseNumber(name, nextNumber);
	}

	@Override
	public void placeNumber(String player, int coordinate, int number) {
		messenger.placeNumber(name, coordinate, number);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public PlayScreen getScreen() {
		return screen;
	}

}
