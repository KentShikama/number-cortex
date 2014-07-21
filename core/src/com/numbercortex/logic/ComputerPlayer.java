package com.numbercortex.logic;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.numbercortex.CortexState;
import com.numbercortex.GameSettings;
import com.numbercortex.logic.brain.Brain;
import com.numbercortex.logic.brain.BrainFactory;

class ComputerPlayer implements Player {

	private String name;
	private Playable screen;
	private GameManager messenger;
	private Brain brain;

	ComputerPlayer(Playable playScreen, GameManager messenger, GameSettings settings) {
		this.screen = playScreen;
		this.messenger = messenger;

		this.brain = buildBrain(messenger, settings);
		this.name = brain.getName();
	}

	private Brain buildBrain(GameManager messenger, GameSettings settings) {
		Brain brain;
		int brainDifficulty = settings.getDifficulty();
		brain = BrainFactory.buildBrain(settings, brainDifficulty);
		return brain;
	}

	@Override
	public void updateState(CortexState state) {
		screen.updateState(state, this);
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> openCoordinates = BoardUtilities.getOpenCoordinates(coordinateNumberMap);
		String winner = state.getWinner();
		if (winner != null || openCoordinates.isEmpty()) {
			return;
		}
		int chosenNumber = state.getChosenNumber();
		if (chosenNumber != -1) {
			final int coordinate = brain.calculateCoordinate(state);
			Action completePlaceNumberAction = buildCompletePlaceNumberAction(coordinate);
			screen.placeNumberWithAnimation(coordinate, completePlaceNumberAction);
		} else {
			int nextNumber = brain.calculateNextNumber(state);
			if (BoardUtilities.getTurnNumber(state) == 0) {
				chooseNumber(null, nextNumber);
			} else {
				Action completeChooseNumberAction = buildCompleteChooseNumberAction(nextNumber);
				screen.chooseNumberWithAnimation(nextNumber, completeChooseNumberAction);
			}
		}
	}
	private Action buildCompletePlaceNumberAction(final int coordinate) {
		Action completePlaceNumberAction = new Action() {
			@Override
			public boolean act(float delta) {
				placeNumber(null, coordinate);
				return true;
			}
		};
		return completePlaceNumberAction;
	}
	private Action buildCompleteChooseNumberAction(final int nextNumber) {
		Action completeChooseNumberAction = new Action() {
			@Override
			public boolean act(float delta) {
				chooseNumber(null, nextNumber);
				return true;
			}
		};
		return completeChooseNumberAction;
	}

	@Override
	public void chooseNumber(String player, int nextNumber) {
		messenger.chooseNumber(name, nextNumber);
	}

	@Override
	public void placeNumber(String player, int coordinate) {
		messenger.placeNumber(name, coordinate);
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
