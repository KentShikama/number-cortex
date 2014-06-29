package com.numbercortex;

import com.badlogic.gdx.Gdx;

import java.util.Map;

class HumanPlayer implements Player {

	private static final String TAG = HumanPlayer.class.getCanonicalName();
	
	private String name;
	private PlayScreen screen;
	private Messenger messenger;
	private CortexState state;

	private int coordinate;
	private boolean nextCoordinateChosen;
	
	private CortexPreferences preferences = CortexPreferences.getInstance();
		
	public HumanPlayer(String name, PlayScreen screen, Messenger messenger) {
		this.messenger = messenger;
		this.screen = screen;
		this.name = name;
	}

	@Override
	public void chooseNumber(String player, int nextNumber) {
		if (state.getChosenNumber() == -1) {
			messenger.chooseNumber(name, nextNumber);
		} else if (state.getChosenNumber() != -1 && nextCoordinateChosen) {
			messenger.placeNumber(name, coordinate);
			messenger.chooseNumber(name, nextNumber);
			nextCoordinateChosen = false;
		} else {
			Gdx.app.log(TAG, "Please place the chosen number first.");
		}
	}

	@Override
	public void placeNumber(String player, int coordinate) {
		this.coordinate = coordinate;
		nextCoordinateChosen = true;
		
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		int chosenNumber = state.getChosenNumber();
		for (Map.Entry<Integer, Integer> entry : coordinateNumberMap.entrySet()) {
			if (entry.getValue() == chosenNumber) {
				coordinateNumberMap.put(entry.getKey(), -1);
			}
		}
		coordinateNumberMap.put(coordinate, chosenNumber);
		int[] winningCoordinates = WinHandler.handleWinningBoard(coordinateNumberMap, preferences);
		if (winningCoordinates != null) {
			messenger.placeNumber(name, coordinate);
		} else {
			CortexState temporaryState = new CortexState.CortexStateBuilder(state.getMessage(), state.getCurrentPlayer(), state.getPlayers(), -1, coordinateNumberMap, state.getAvailableNumbers()).build();
			screen.updateState(temporaryState);
		}
	}

	@Override
	public void updateState(CortexState state) {
		this.state = state;
		screen.updateState(state);
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