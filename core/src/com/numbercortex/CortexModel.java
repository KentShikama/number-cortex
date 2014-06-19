package com.numbercortex;

public interface CortexModel {
	public void placeNumber (String playerName, int number, int coordinate);
	public void chooseNumber (String playerName, int nextNumber);
	public void register(String playerName);
	public void setExchangeable(Exchangeable local);
}
