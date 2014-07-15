package com.numbercortex.logic;

import java.util.ArrayList;
import java.util.Map;

public class BoardUtilities {
	public static ArrayList<Integer> getOpenCoordinates(Map<Integer, Integer> coordinateNumberMap) {
		ArrayList<Integer> openCoordinates = new ArrayList<Integer>();
		for (Map.Entry<Integer, Integer> entry : coordinateNumberMap.entrySet()) {
			if (entry.getValue() == -1) {
				int openCoordinate = entry.getKey();
				openCoordinates.add(openCoordinate);
			}
		}
		return openCoordinates;
	}

	public static int getTurnNumber(CortexState state, int numberOfRows) {
		int fullCoordinatesSize = getFullCoordinates(state, numberOfRows);
		int turnCount = 0;
		int chosenNumber = state.getChosenNumber();
		if (chosenNumber == -1) {
			turnCount = fullCoordinatesSize * 2;
		} else {
			turnCount = fullCoordinatesSize * 2 + 1;
		}
		return turnCount;
	}
	private static int getFullCoordinates(CortexState state, int numberOfRows) {
		int boardSize = getBoardSize(numberOfRows);
		int openCoordinatesSize = getOpenCoordinateSize(state);
		int fullCoordinatesSize = boardSize - openCoordinatesSize;
		return fullCoordinatesSize;
	}
	private static int getBoardSize(int numberOfRows) {
		int boardSize = numberOfRows * numberOfRows;
		return boardSize;
	}
	private static int getOpenCoordinateSize(CortexState state) {
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> openCoordinates = BoardUtilities.getOpenCoordinates(coordinateNumberMap);
		int openCoordinatesSize = openCoordinates.size();
		return openCoordinatesSize;
	}
}
