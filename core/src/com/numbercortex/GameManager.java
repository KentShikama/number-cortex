package com.numbercortex;

public interface GameManager extends Sendable, Receivable {
	public abstract GameSettings getSettings();
	public abstract void startNewGame();
	public abstract void resumeGame();
}
