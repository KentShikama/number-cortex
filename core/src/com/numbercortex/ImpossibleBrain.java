package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

public class ImpossibleBrain implements Brain {

	private String name = "Impossible AI";
	private BrainUtilities utility;

	public ImpossibleBrain(GameSettings settings) {
		this.utility = new BrainUtilities(settings);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int calculateCoordinate(CortexState state) {
		int chosenNumber = state.getChosenNumber();
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> openCoordinates = utility.getOpenCoordinates(coordinateNumberMap);

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
			ArrayList<Integer> openCoordinates = utility.getOpenCoordinates(coordinateNumberMap);
			if (openCoordinates.size() > 9) {
				nextNumber = utility.assignRandomNumberFromList(safeNumbers);
				return nextNumber;
			}
			int maxPoints = 0;
			ArrayList<Integer> bestSafeNumberList = new ArrayList<Integer>();
			for (Integer safeNumber : safeNumbers) {
				int points = 0;
				availableNumbers.remove(Integer.valueOf(safeNumber));
				for (Integer openCoordinate : openCoordinates) {
					coordinateNumberMap.put(openCoordinate, safeNumber); // Opponent places your number
					ArrayList<Integer> safeNumbersOpponentCanChoose = utility.getSafeNumbersIfExistent(
							coordinateNumberMap, availableNumbers);
					if (safeNumbersOpponentCanChoose.isEmpty()) {
						// Your opponent will not choose this coordinate
					} else {
						ArrayList<Integer> newAvailableNumbers = (ArrayList<Integer>) safeNumbersOpponentCanChoose
								.clone();
						for (Integer possibleNextNumber : safeNumbersOpponentCanChoose) { // Opponent chooses your number
							boolean safe = false;
							newAvailableNumbers.remove(Integer.valueOf(possibleNextNumber));
							ArrayList<Integer> newOpenCoordinates = utility.getOpenCoordinates(coordinateNumberMap);
							for (Integer newOpenCoordinate : newOpenCoordinates) {
								coordinateNumberMap.put(newOpenCoordinate, possibleNextNumber); // You place number
								ArrayList<Integer> list = utility.getSafeNumbersIfExistent(coordinateNumberMap,
										newAvailableNumbers); // Check if you can give a safe number
								coordinateNumberMap.put(newOpenCoordinate, -1);
								if (!list.isEmpty()) { // If you can give a safe number...then that number is good
									safe = true;
									break;
								}
							}
							newAvailableNumbers.add(Integer.valueOf(possibleNextNumber));
							if (safe) { // Safe for you if opponent decides to give you this possibleNextNumber
								points++;
							}
						}
					}
					coordinateNumberMap.put(openCoordinate, -1);
				}
				availableNumbers.add(safeNumber);
				if (points > maxPoints) {
					maxPoints = points;
					bestSafeNumberList.clear();
					bestSafeNumberList.add(safeNumber);
				} else if (points == maxPoints) {
					bestSafeNumberList.add(safeNumber);
				}
			}
			if (bestSafeNumberList.isEmpty()) {
				nextNumber = utility.assignRandomNumberFromList(safeNumbers);
			} else {
				nextNumber = utility.assignRandomNumberFromList(bestSafeNumberList);
			}
		}
		return nextNumber;
	}

}
