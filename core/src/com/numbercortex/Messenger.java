package com.numbercortex;

public interface Messenger extends Sendable, Receivable {
	public abstract void register(Player... players);
	public abstract GameSettings getSettings();
}
