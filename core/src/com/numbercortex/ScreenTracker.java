package com.numbercortex;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Screen;

public class ScreenTracker {
	
	private ScreenTracker() {}

	public static TitleScreen titleScreen;
	public static LevelsScreen levelsScreen;
	public static SinglePlayerSettingsScreen singlePlayerSettingsScreen;
	public static TwoPlayerSettingsScreen twoPlayerSettingsScreen;
	public static PlayScreen playScreen;
	private static Map<String, Screen> screenMap;
	public static Screen getScreen(String name) {
		if (screenMap == null) {
			screenMap = new HashMap<String, Screen>();
			screenMap.put(TitleScreen.TAG, titleScreen);
			screenMap.put(LevelsScreen.TAG, levelsScreen);
			screenMap.put(SinglePlayerSettingsScreen.TAG, singlePlayerSettingsScreen);
			screenMap.put(TwoPlayerSettingsScreen.TAG, twoPlayerSettingsScreen);
			screenMap.put(PlayScreen.TAG, playScreen);
		}
		if (name.isEmpty()) {
			return titleScreen;
		} else {
			Screen screen = screenMap.get(name);	
			return screen;			
		}
	}
	
	public static void dispose() {
		levelsScreen.dispose();
		singlePlayerSettingsScreen.dispose();
		twoPlayerSettingsScreen.dispose();
		screenMap = null;
	}
	
	public static enum Mode {
		SINGLE_PLAYER, TWO_PLAYER;
	}
	private static Map<String, Mode> modeMap = new HashMap<String, Mode>();
	static {
		modeMap.put(Mode.SINGLE_PLAYER.name(), Mode.SINGLE_PLAYER);
		modeMap.put(Mode.TWO_PLAYER.name(), Mode.TWO_PLAYER);
	}
	public static Mode getMode(String mode) {
		return modeMap.get(mode);			
	}
	protected static Mode mode;
	
	public static boolean isInPlay;
}
