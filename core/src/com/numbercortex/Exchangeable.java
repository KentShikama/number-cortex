package com.numbercortex;

public interface Exchangeable {
	public void update(CortexState state);
	public void register(Player player);
	void chooseNumber(int nextNumber);
	void placeNumber(int coordinate, int number);
}
