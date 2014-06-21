package com.numbercortex;

public interface CortexModel {
	public abstract void chooseNumber(String playerName, int nextNumber);
	public abstract void placeNumber(String playerName, int coordinate, int number);
	public abstract void register(String username);
}
