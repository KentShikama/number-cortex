package com.numbercortex.view;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Screen;
import com.numbercortex.Launch;

public class ScreenTracker {

	private ScreenTracker() {}

	public static TitleScreen titleScreen;
	public static LevelsScreen levelsScreen;
	public static SinglePlayerSettingsScreen singlePlayerSettingsScreen;
	public static TwoPlayerSettingsScreen twoPlayerSettingsScreen;
	public static PlayScreen playScreen;

	public static void initializeScreens(Launch game) {
		titleScreen = new TitleScreen(game);
		singlePlayerSettingsScreen = new SinglePlayerSettingsScreen(game);
		twoPlayerSettingsScreen = new TwoPlayerSettingsScreen(game);
		levelsScreen = new LevelsScreen(game);
		playScreen = new PlayScreen(game);
	}

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
		playScreen.dispose();
		levelsScreen.dispose();
		singlePlayerSettingsScreen.dispose();
		twoPlayerSettingsScreen.dispose();
		titleScreen = null;
		singlePlayerSettingsScreen = null;
		twoPlayerSettingsScreen = null;
		levelsScreen = null;
		playScreen = null;
		screenMap = null;
	}
}
