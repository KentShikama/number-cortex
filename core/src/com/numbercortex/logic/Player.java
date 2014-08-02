package com.numbercortex.logic;

public interface Player extends Sendable, Receivable {
    public abstract String getName();
    public abstract Playable getScreen();
}
