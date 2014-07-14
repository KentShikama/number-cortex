package com.numbercortex;

public class ScreenTracker {
	public static TitleScreen titleScreen;
	public static LevelsScreen levelsScreen;
	public static SinglePlayerSettingsScreen singlePlayerSettingsScreen;
	public static TwoPlayerSettingsScreen twoPlayerSettingsScreen;
	public static PlayScreen playScreen;

	protected static Mode mode;
	public static boolean isInPlay;
	
	public static int level;

	public static enum Mode {
		SINGLE_PLAYER, TWO_PLAYER;
	}

	private ScreenTracker() {}
}
