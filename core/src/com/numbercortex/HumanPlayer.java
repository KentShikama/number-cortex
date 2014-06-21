package com.numbercortex;

class HumanPlayer implements Player {
	
	private String name;
	private PlayScreen screen;
	private Exchangeable exchangeable;

	HumanPlayer(Exchangeable exchangeable, PlayScreen screen, String name) {
		this.exchangeable = exchangeable;
		this.screen = screen;
		this.name = name;
		exchangeable.register(this);
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public PlayScreen getScreen() {
		return screen;
	}

	@Override
	public void chooseNumber(int nextNumber) {
		exchangeable.chooseNumber(name, nextNumber);
	}

	@Override
	public void placeNumber(int coordinate, int number) {
		exchangeable.placeNumber(name, coordinate, number);
	}
	
}