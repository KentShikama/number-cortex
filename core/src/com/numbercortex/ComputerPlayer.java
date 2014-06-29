package com.numbercortex;

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
		if (state.getWinner() != null) {
			screen.updateState(state);
			return;
		}
		int chosenNumber = state.getChosenNumber();
		if (chosenNumber != -1) {
			int coordinate = brain.calculateCoordinate(state);
			placeNumber(null, coordinate, chosenNumber);
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
	public void placeNumber(String player, int coordinate, int number) {
		messenger.placeNumber(name, coordinate, number);
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
