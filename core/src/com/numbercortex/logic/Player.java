package com.numbercortex.logic;

import com.numbercortex.view.PlayScreen;

public interface Player extends Sendable, Receivable {
	public abstract String getName();
	public abstract PlayScreen getScreen();
}
