package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

public class BrainUtilities {
	private BrainUtilities() {}
	
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
	
	public static boolean isWinningCoordinate(int coordinate, int number, Map<Integer, Integer> coordinateNumberMap, CortexPreferences preferences) {
		coordinateNumberMap.put(coordinate, number);
		int[] winningCoordinates = WinHandler.handleWinningBoard(coordinateNumberMap, preferences);
		coordinateNumberMap.replace(coordinate, -1);
		if (winningCoordinates != null) {
			return true;
		}
		return false;
	}
	
}
