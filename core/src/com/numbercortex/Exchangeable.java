package com.numbercortex;

public interface Exchangeable {
	public abstract void chooseNumber(String playerName, int nextNumber);
	public abstract void placeNumber(String playerName, int coordinate, int number);
	public abstract void register(Player... players);
	public abstract void updateState(CortexState state);
}
