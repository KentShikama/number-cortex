package com.numbercortex;

public interface CortexModel extends Sendable {
	public abstract void register(String username);
	public abstract void setFirstPlayerPosition(int firstPlayerNumber);
}
