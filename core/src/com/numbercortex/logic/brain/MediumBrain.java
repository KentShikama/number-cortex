package com.numbercortex.logic.brain;

import java.util.ArrayList;
import java.util.Map;
import com.numbercortex.CortexState;
import com.numbercortex.GameSettings;
import com.numbercortex.logic.BoardUtilities;

class MediumBrain implements Brain {

    private String name = "Average (AI)";
    private BrainCalculator utility;

    MediumBrain(GameSettings settings) {
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

        int chosenCoordinate = utility.assignWinningCoordinateIfExistent(chosenNumber, coordinateNumberMap, openCoordinates);
        if (chosenCoordinate == -1) {
            chosenCoordinate = utility.assignRandomNumberFromList(openCoordinates);
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
            nextNumber = utility.assignRandomNumberFromList(safeNumbers);
        }
        return nextNumber;
    }

}
