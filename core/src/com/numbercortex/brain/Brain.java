package com.numbercortex.brain;

import com.numbercortex.logic.CortexState;

public interface Brain {
	public abstract String getName();
	public abstract int calculateCoordinate(CortexState state);
	public abstract int calculateNextNumber(CortexState state);
}
