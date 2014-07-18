package com.numbercortex.logic;

import com.numbercortex.CortexState;

public interface GameManager extends Sendable, Receivable {
	public abstract CortexState getState();
	public abstract void startNewGame();
	public abstract void resumeGame();
}
