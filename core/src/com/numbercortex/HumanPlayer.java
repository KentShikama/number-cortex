package com.numbercortex;

class HumanPlayer implements Player {
	
	private String name;
	private PlayScreen screen;
	private Exchangeable exchangeable;

	public HumanPlayer(String name, PlayScreen screen, Exchangeable exchangeable) {
		this.exchangeable = exchangeable;
		this.screen = screen;
		this.name = name;
	}
	
	@Override
	public void chooseNumber(String player, int nextNumber) {
		exchangeable.chooseNumber(name, nextNumber);
	}

	@Override
	public void placeNumber(String player, int coordinate, int number) {
		exchangeable.placeNumber(name, coordinate, number);
	}

	@Override
	public void updateState(CortexState state) {
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