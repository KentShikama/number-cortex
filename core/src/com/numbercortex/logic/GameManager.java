package com.numbercortex.logic;

import com.numbercortex.CortexState;
import com.numbercortex.GameSettings;

public interface GameManager extends Sendable, Receivable {
	public abstract GameSettings getSettings();
	public abstract CortexState getState();
	public abstract void startNewGame();
	public abstract void resumeGame();
}
