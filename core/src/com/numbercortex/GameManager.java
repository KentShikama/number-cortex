package com.numbercortex;

public interface GameManager extends Sendable, Receivable {
	public abstract GameSettings getSettings();
	public abstract CortexState getState();
	public abstract void startNewGame();
	public abstract void resumeGame();
}
