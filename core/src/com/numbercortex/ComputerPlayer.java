package com.numbercortex;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.numbercortex.brain.Brain;
import com.numbercortex.brain.BrainFactory;
import com.numbercortex.view.PlayScreen;

public class ComputerPlayer implements Player {

	private String name;
	private PlayScreen playScreen;
	private GameManager messenger;
	private Brain brain;

	public ComputerPlayer(PlayScreen playScreen, GameManager messenger) {
		this.playScreen = playScreen;
		this.messenger = messenger;

		this.brain = buildBrain(messenger);
		this.name = brain.getName();
	}

	private Brain buildBrain(GameManager messenger) {
		Brain brain;
		GameSettings settings = messenger.getSettings();
		int brainDifficulty = settings.getDifficulty();
		brain = BrainFactory.buildBrain(settings, brainDifficulty);
		return brain;
	}

	@Override
	public void updateState(CortexState state) {
		playScreen.updateState(state, this);
		if (state.getWinner() != null) {
			return;
		}
		int chosenNumber = state.getChosenNumber();
		if (chosenNumber != -1) {
			final int coordinate = brain.calculateCoordinate(state);
			placeNumberWithAnimation(coordinate);
		} else {
			int nextNumber = brain.calculateNextNumber(state);
			chooseNumber(null, nextNumber);
		}
	}
	private void placeNumberWithAnimation(final int coordinate) {
		ArrayList<Object> components = playScreen.getRequiredComponentsForComputerAnimation(coordinate);
		Label labelToMove = (Label) components.get(0);
		MoveToAction moveToAction = (MoveToAction) components.get(1);
		DelayAction delayAction = Actions.delay(0.5f);
		Action completePlaceNumberAction = buildCompletePlaceNumberAction(coordinate);
		SequenceAction placeNumberAction = Actions.sequence(delayAction, moveToAction, completePlaceNumberAction);
		labelToMove.addAction(placeNumberAction);
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
	public PlayScreen getScreen() {
		return playScreen;
	}

}
