package com.numbercortex;

public interface Player extends Sendable, Receivable {
	public abstract String getName();
	public abstract PlayScreen getScreen();
}
