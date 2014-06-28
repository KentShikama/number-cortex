package com.numbercortex;

public class ScreenTracker {
	public static SettingsScreen settingsScreen;
	public static PlayScreen playScreen;
	public static boolean isInPlay = false;
	
	protected static Mode mode;
	public static enum Mode {
		SINGLE_PLAYER, TWO_PLAYER, ONLINE;
	}
	
	private ScreenTracker() {}
}
