package com.numbercortex.logic.brain;

import java.util.ArrayList;
import java.util.Map;
import com.numbercortex.CortexState;
import com.numbercortex.GameSettings;
import com.numbercortex.logic.BoardUtilities;

class RandomBrain implements Brain {

    private String name = "Gary (AI)";
    private BrainCalculator utility;

    RandomBrain(GameSettings settings) {
        this.utility = new BrainCalculator(settings);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int calculateCoordinate(CortexState state) {
        Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
        ArrayList<Integer> openCoordinates = BoardUtilities.getOpenCoordinates(coordinateNumberMap);
        int chosenCoordinate = utility.assignRandomNumberFromList(openCoordinates);
        return chosenCoordinate;
    }

    @Override
    public int calculateNextNumber(CortexState state) {
        ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
        return utility.assignRandomNumberFromList(availableNumbers);
    }

}
