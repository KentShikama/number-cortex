package com.numbercortex;

public interface Exchangeable extends Sendable, Receivable{
	public abstract void register(Player... players);
}
