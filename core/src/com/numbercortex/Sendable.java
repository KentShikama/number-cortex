package com.numbercortex;

public interface Sendable {
	public abstract void chooseNumber(String player, int nextNumber);
	public abstract void placeNumber(String player, int coordinate);
}