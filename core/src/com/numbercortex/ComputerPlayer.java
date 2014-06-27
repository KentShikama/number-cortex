package com.numbercortex;

public class ComputerPlayer implements Player {

	private String name;
	private PlayScreen screen;

	public ComputerPlayer(String name, PlayScreen playScreen, Exchangeable messenger) {
		this.name = name;
		this.screen = playScreen;
	}
	
	/**
	 * AI function and which calls chooseNumber or placeNumber once computing is finished
	 */
	@Override
	public void updateState(CortexState state) {
		
	}
	
	@Override
	public void chooseNumber(String player, int nextNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void placeNumber(String player, int coordinate, int number) {
		// TODO Auto-generated method stub
		
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
