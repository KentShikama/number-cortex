package com.numbercortex;

import java.util.ArrayList;

public interface Exchangeable {
	public void update(CortexState state);
	public ArrayList<String> getRegisteredPlayerNames();
	public void register(Player player);
}
