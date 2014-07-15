package com.numbercortex.brain;

import java.util.ArrayList;
import java.util.Map;

import com.numbercortex.BoardUtilities;
import com.numbercortex.CortexState;
import com.numbercortex.GameSettings;

public class ImpossibleBrain implements Brain {

	private String name = "Impossible AI";
	private BrainCalculator utility;

	ImpossibleBrain(GameSettings settings) {
		this.utility = new BrainCalculator(settings);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int calculateCoordinate(CortexState state) {
		int chosenNumber = state.getChosenNumber();
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> openCoordinates = BoardUtilities.getOpenCoordinates(coordinateNumberMap);

		int chosenCoordinate = utility.assignWinningCoordinateIfExistent(chosenNumber, coordinateNumberMap,
				openCoordinates);
		if (chosenCoordinate == -1) {
			ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
			ArrayList<Integer> safeCoordinates = utility.getSafeCoordinatesIfExistent(chosenNumber,
					coordinateNumberMap, openCoordinates, availableNumbers);
			if (safeCoordinates.isEmpty()) {
				chosenCoordinate = utility.assignRandomNumberFromList(openCoordinates);
			} else {
				chosenCoordinate = utility.assignRandomNumberFromList(safeCoordinates);
			}
		}
		return chosenCoordinate;
	}

	@Override
	public int calculateNextNumber(CortexState state) {
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> availableNumbers = state.getAvailableNumbers();

		ArrayList<Integer> safeNumbers = utility.getSafeNumbersIfExistent(coordinateNumberMap, availableNumbers);
		int nextNumber;
		if (safeNumbers.isEmpty()) {
			nextNumber = utility.assignRandomNumberFromList(availableNumbers);
		} else {
			nextNumber = getSafestNumber(safeNumbers, coordinateNumberMap, availableNumbers);
		}
		return nextNumber;
	}
	private int getSafestNumber(ArrayList<Integer> safeNumbers, Map<Integer, Integer> coordinateNumberMap,
			ArrayList<Integer> availableNumbers) {
		int safestNumber;
		ArrayList<Integer> openCoordinates = BoardUtilities.getOpenCoordinates(coordinateNumberMap);
		if (openCoordinates.size() > 9) {
			safestNumber = utility.assignRandomNumberFromList(safeNumbers);
		} else {
			int maxPoints = 0;
			ArrayList<Integer> safestNumberList = new ArrayList<Integer>();
			for (Integer safeNumber : safeNumbers) {
				int points = calculatePoints(safeNumber, coordinateNumberMap, availableNumbers, openCoordinates);
				maxPoints = updateSafestNumberListAndMaxPoints(safeNumber, points, safestNumberList, maxPoints);
			}
			safestNumber = utility.assignRandomNumberFromList(safestNumberList);
		}
		return safestNumber;
	}
	private int calculatePoints(Integer safeNumber, Map<Integer, Integer> coordinateNumberMap,
			ArrayList<Integer> availableNumbers, ArrayList<Integer> openCoordinates) {
		int points = 0;
		availableNumbers.remove(Integer.valueOf(safeNumber));
		for (Integer openCoordinate : openCoordinates) {
			coordinateNumberMap.put(openCoordinate, safeNumber); // Opponent places my number
			ArrayList<Integer> safeNumbersOpponentCanChoose = utility.getSafeNumbersIfExistent(coordinateNumberMap,
					availableNumbers);
			if (safeNumbersOpponentCanChoose.isEmpty()) {
				// Your opponent will not choose this coordinate
			} else {
				@SuppressWarnings("unchecked")
				ArrayList<Integer> newAvailableNumbers = (ArrayList<Integer>) safeNumbersOpponentCanChoose.clone();
				for (Integer possibleNextNumber : safeNumbersOpponentCanChoose) { // Opponent chooses my number
					boolean safe = checkIfSafe(possibleNextNumber, coordinateNumberMap, newAvailableNumbers);
					if (safe) {
						points++;
					}
				}
			}
			coordinateNumberMap.put(openCoordinate, -1);
		}
		availableNumbers.add(safeNumber);
		return points;
	}
	private boolean checkIfSafe(Integer possibleNextNumber, Map<Integer, Integer> coordinateNumberMap,
			ArrayList<Integer> newAvailableNumbers) {
		boolean safe = false;
		newAvailableNumbers.remove(Integer.valueOf(possibleNextNumber));
		ArrayList<Integer> newOpenCoordinates = BoardUtilities.getOpenCoordinates(coordinateNumberMap);
		for (Integer newOpenCoordinate : newOpenCoordinates) {
			coordinateNumberMap.put(newOpenCoordinate, possibleNextNumber); // You place number
			ArrayList<Integer> list = utility.getSafeNumbersIfExistent(coordinateNumberMap, newAvailableNumbers); // Check if you can give a safe number
			coordinateNumberMap.put(newOpenCoordinate, -1);
			if (!list.isEmpty()) { // If you can give a safe number...then that number is good
				safe = true;
				break;
			}
		}
		newAvailableNumbers.add(Integer.valueOf(possibleNextNumber));
		return safe;
	}
	private int updateSafestNumberListAndMaxPoints(Integer safeNumber, int points, ArrayList<Integer> safestNumberList,
			int maxPoints) {
		if (points > maxPoints) {
			maxPoints = points;
			safestNumberList.clear();
			safestNumberList.add(safeNumber);
		} else if (points == maxPoints) {
			safestNumberList.add(safeNumber);
		}
		return maxPoints;
	}
}
