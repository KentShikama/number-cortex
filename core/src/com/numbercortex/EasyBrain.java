package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

public class EasyBrain implements Brain {
	
	public int calculateCoordinate(CortexState state) {
		ArrayList<Integer> openCoordinates = new ArrayList<Integer>();
		for (Map.Entry<Integer, Integer> entry : state.getCoordinateNumberMap().entrySet()) {
			if (entry.getValue() == -1) {
				int openCoordinate = entry.getKey();
				openCoordinates.add(openCoordinate);
			}
		}
		int chosenCoordinatePosition = (int) (Math.random() * openCoordinates.size());
		int chosenCoordinate = openCoordinates.get(chosenCoordinatePosition);
		return chosenCoordinate;
	}
	
	public int calculateNextNumber(CortexState state) {
		ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
		int nextNumberPosition = (int) (Math.random() * availableNumbers.size());
		int nextNumber = availableNumbers.get(nextNumberPosition);
		return nextNumber;
	}
	
}
