package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

class HumanPlayer implements Player {
	
	private String name;
	private PlayScreen screen;

	HumanPlayer(PlayScreen screen, String name) {
		this.screen = screen;
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void updateState(CortexState state) {
		updateMessageArea(state);
		updateBoardMap(state);
		updateNumberScroller(state);
	}
	
	private void updateMessageArea(CortexState state) {
		String currentPlayer = state.getCurrentPlayer();
		int chosenNumber = state.getChosenNumber();
		screen.updateMessageArea(currentPlayer, chosenNumber);
	}
	
	private void updateBoardMap(CortexState state) {
		Map<Integer, Integer> boardMap = state.getCoordinateNumberMap();
		for (Map.Entry<Integer, Integer> entry : boardMap.entrySet()) {
			int coordinate = entry.getKey();
			int number = entry.getValue();
			if (number != -1) {
				screen.updateBoardCell(coordinate, number);
			}
		}
	}

	private void updateNumberScroller(CortexState state) {
		ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
		screen.updateNumberScroller(availableNumbers);
	}
}