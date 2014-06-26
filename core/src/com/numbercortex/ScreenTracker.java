package com.numbercortex;

public class ScreenTracker {
	public static SettingsScreen settingsScreen;
	public static PlayScreen playScreen;
	public static boolean isInPlay = false;
	
	public static enum MODE {
		SINGLE, MULTIPLAYER, ONLINE;
	}
}
