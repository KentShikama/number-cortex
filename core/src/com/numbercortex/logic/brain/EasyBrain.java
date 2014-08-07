package com.numbercortex.logic.brain;

import java.util.ArrayList;
import java.util.Map;
import com.numbercortex.CortexState;
import com.numbercortex.GameSettings;
import com.numbercortex.logic.BoardUtilities;

class EasyBrain implements Brain {

    private BrainCalculator utility;
    private String name = "Peasy (AI)";

    EasyBrain(GameSettings settings) {
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
        ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
        return utility.assignRandomNumberFromList(availableNumbers);
    }

}
