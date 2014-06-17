package com.numbercortex;

public interface CortexModel {
	public void register(CortexScreen screen);
	public void startGame();
	public void placeNumber (int number, int coordinate);
	public void chooseNumber (int nextNumber);
}
