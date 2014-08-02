package com.numbercortex.logic;

import com.numbercortex.CortexState;
import com.numbercortex.GameSettings;
import com.numbercortex.ModeTracker;
import com.numbercortex.Persistence;
import com.numbercortex.view.DragAndDropHandler;
import com.numbercortex.view.Sound;
import java.util.Map;

class HumanPlayer implements Player, InteractableSendable {

    private String name;
    private Playable screen;
    private GameManager messenger;
    private CortexState state;

    private int savedCoordinate;
    private int savedChosenNumber;
    private boolean nextCoordinateChosen;

    private WinHandler winHandler;

    HumanPlayer(String name, Playable screen, GameManager messenger, GameSettings settings) {
        this.messenger = messenger;
        this.screen = screen;
        this.name = name;
        this.winHandler = new WinHandler(settings);
    }

    @Override
    public void chooseNumber(String player, int nextNumber) {
        DragAndDropHandler.getInstance().resetPlacementCount();
        int chosenNumber = state.getChosenNumber();
        if (chosenNumber == -1 && !nextCoordinateChosen) {
            Sound.click();
            messenger.chooseNumber(name, nextNumber);
        } else if (state.getChosenNumber() == -1 && nextCoordinateChosen) {
            Sound.click();
            messenger.placeNumber(name, savedCoordinate);
            messenger.chooseNumber(name, nextNumber);
            nextCoordinateChosen = false;
        } else {
            Sound.missClick();
            screen.flashChosenNumber(chosenNumber);
        }
    }

    @Override
    public void handleConfirmedSingleTap(int tappedNumber) {
        int chosenNumber = state.getChosenNumber();
        if (chosenNumber != -1 && !nextCoordinateChosen) {
            Sound.missClick();
            screen.flashChosenNumber(chosenNumber);
        }
    }

    @Override
    public void placeNumber(String player, int coordinate) {
        if (ModeTracker.mode == ModeTracker.Mode.SINGLE_PLAYER && Persistence.getInstance().getCurrentLevel() == 0) {
            messenger.placeNumber(name, coordinate);
        } else if (DragAndDropHandler.getInstance().getPlacementCount() > DragAndDropHandler.MAXIMUM_PLACEMENT_COUNT) {
            messenger.placeNumber(name, coordinate);
        } else {
            savePostPlaceState(coordinate);
            postPlaceUpdate(coordinate);
        }
    }
    private void savePostPlaceState(int coordinate) {
        this.savedCoordinate = coordinate;
        nextCoordinateChosen = true;
        int chosenNumber = state.getChosenNumber();
        if (chosenNumber != -1) {
            savedChosenNumber = chosenNumber;
        }
    }
    private void postPlaceUpdate(int coordinate) {
        Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
        updateCoordinateNumberMap(coordinate, coordinateNumberMap);
        handleUpdatedMap(coordinate, coordinateNumberMap);
    }
    private void updateCoordinateNumberMap(int coordinate, Map<Integer, Integer> coordinateNumberMap) {
        int chosenNumber = savedChosenNumber;
        eliminateOldChosenNumberPosition(coordinateNumberMap, chosenNumber);
        coordinateNumberMap.put(coordinate, chosenNumber);
    }
    private void eliminateOldChosenNumberPosition(Map<Integer, Integer> coordinateNumberMap, int chosenNumber) {
        for (Map.Entry<Integer, Integer> entry : coordinateNumberMap.entrySet()) {
            if (entry.getValue() == chosenNumber) {
                coordinateNumberMap.put(entry.getKey(), -1);
            }
        }
    }
    private void handleUpdatedMap(int coordinate, Map<Integer, Integer> coordinateNumberMap) {
        int[] winningValues = winHandler.handleWinningBoard(coordinateNumberMap);
        if (winningValues != null) {
            messenger.placeNumber(name, coordinate);
        } else {
            CortexState temporaryState = new CortexState.CortexStateBuilder(state.getMessage(), state.getCurrentPlayer(), state.getPlayers(), -1, coordinateNumberMap, state.getAvailableNumbers()).build();
            messenger.setState(temporaryState);
        }
    }

    @Override
    public void updateState(CortexState state) {
        this.state = state;
        screen.updateState(state, this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Playable getScreen() {
        return screen;
    }

}