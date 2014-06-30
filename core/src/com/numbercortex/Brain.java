package com.numbercortex;

public interface Brain {
	public abstract String getName();
	public abstract int calculateCoordinate(CortexState state);
	public abstract int calculateNextNumber(CortexState state);
}
