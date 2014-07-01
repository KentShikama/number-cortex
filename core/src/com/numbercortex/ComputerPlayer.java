package com.numbercortex;

import java.util.ArrayList;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class ComputerPlayer implements Player {

	private String name;
	private PlayScreen screen;
	private Messenger messenger;
	private Brain brain;

	public ComputerPlayer(String name, PlayScreen playScreen, Messenger messenger, Brain difficulty) {
		this.name = name;
		this.screen = playScreen;
		this.messenger = messenger;
		this.brain = difficulty;
	}

	@Override
	public void updateState(CortexState state) {
		screen.updateState(state);
		if (state.getWinner() != null) {
			return;
		}
		int chosenNumber = state.getChosenNumber();
		if (chosenNumber != -1) {
			final int coordinate = brain.calculateCoordinate(state);

			ArrayList<Object> components = screen.getRequiredComponentsForComputerAnimation(coordinate);
			Label labelToMove = (Label) components.get(0);
			MoveToAction moveToAction = (MoveToAction) components.get(1);
			DelayAction delayAction = Actions.delay(0.5f);
			Action completeAction = new Action() {
				@Override
				public boolean act(float delta) {
					placeNumber(null, coordinate);
					return true;
				}
			};
			labelToMove.addAction(Actions.sequence(delayAction, moveToAction, completeAction));
		} else {
			int nextNumber = brain.calculateNextNumber(state);
			chooseNumber(null, nextNumber);
		}
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
		return screen;
	}

}
