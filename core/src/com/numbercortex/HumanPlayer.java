package com.numbercortex;

import com.badlogic.gdx.Gdx;

import java.util.Map;

class HumanPlayer implements Player {

	private static final String TAG = HumanPlayer.class.getCanonicalName();

	private String name;
	private PlayScreen screen;
	private GameManager messenger;
	private CortexState state;

	private int savedCoordinate;
	private boolean nextCoordinateChosen;

	public HumanPlayer(String name, PlayScreen screen, GameManager messenger) {
		this.messenger = messenger;
		this.screen = screen;
		this.name = name;
	}

	/**
	 * TODO: Eventually send the state along with a confirm dialog
	 */
	@Override
	public void chooseNumber(String player, int nextNumber) {
		if (state.getChosenNumber() == -1) {
			messenger.chooseNumber(name, nextNumber);
		} else if (state.getChosenNumber() != -1 && nextCoordinateChosen) {
			messenger.placeNumber(name, savedCoordinate);
			messenger.chooseNumber(name, nextNumber);
			nextCoordinateChosen = false;
		} else {
			Gdx.app.log(TAG, "Please place the chosen number first.");
		}
	}

	@Override
	public void placeNumber(String player, int coordinate) {
		if (ScreenTracker.level == 0) {
			messenger.placeNumber(name, coordinate);
		} else {
			savePostPlaceState(coordinate);
			postPlaceUpdate(coordinate);	
		}
	}
	private void savePostPlaceState(int coordinate) {
		this.savedCoordinate = coordinate;
		nextCoordinateChosen = true;
	}
	private void postPlaceUpdate(int coordinate) {
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		updateCoordinateNumberMap(coordinate, coordinateNumberMap);
		handleUpdatedMap(coordinate, coordinateNumberMap);
	}
	private void updateCoordinateNumberMap(int coordinate, Map<Integer, Integer> coordinateNumberMap) {
		int chosenNumber = state.getChosenNumber();
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
		int[] winningValues = WinHandler.handleWinningBoard(coordinateNumberMap, messenger.getSettings());
		if (winningValues != null) {
			messenger.placeNumber(name, coordinate);
		} else {
			CortexState temporaryState = new CortexState.CortexStateBuilder(state.getMessage(),
					state.getCurrentPlayer(), state.getPlayers(), -1, coordinateNumberMap, state.getAvailableNumbers())
					.build();
			screen.updateState(temporaryState, this);
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
	public PlayScreen getScreen() {
		return screen;
	}

}