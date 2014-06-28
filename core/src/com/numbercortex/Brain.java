package com.numbercortex;

public interface Brain {
	public abstract int calculateCoordinate(CortexState state);
	public abstract int calculateNextNumber(CortexState state);
}
