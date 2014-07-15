package com.numbercortex;

import com.numbercortex.view.PlayScreen;

public interface Player extends Sendable, Receivable {
	public abstract String getName();
	public abstract PlayScreen getScreen();
}
