package com.numbercortex.view;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Screen;

public class ScreenTracker {

	private ScreenTracker() {}

	public static TitleScreen titleScreen;
	public static MoreScreen moreScreen;
	public static CreditsScreen creditsScreen;
	public static LevelsScreen levelsScreen;
	public static SinglePlayerSettingsScreen singlePlayerSettingsScreen;
	public static TwoPlayerSettingsScreen twoPlayerSettingsScreen;
	public static PlayScreen playScreen;
	public static OptionsScreen optionsScreen;
	
	public static TransitionScreen transitionScreen;

	public static void initializeScreens(Launch game) {
		titleScreen = new TitleScreen(game);
		moreScreen = new MoreScreen(game);
		creditsScreen = new CreditsScreen(game);
		singlePlayerSettingsScreen = new SinglePlayerSettingsScreen(game);
		twoPlayerSettingsScreen = new TwoPlayerSettingsScreen(game);
		levelsScreen = new LevelsScreen(game);
		playScreen = new PlayScreen(game);
		optionsScreen = new OptionsScreen(game);
		
		transitionScreen = new TransitionScreen(game);
	}

	private static Map<String, Screen> screenMap;
	public static Screen getScreen(String name) {
		if (screenMap == null) {
			screenMap = new HashMap<String, Screen>();
			screenMap.put(TitleScreen.TAG, titleScreen);
			screenMap.put(MoreScreen.TAG, moreScreen);
			screenMap.put(CreditsScreen.TAG, creditsScreen);
			screenMap.put(LevelsScreen.TAG, levelsScreen);
			screenMap.put(SinglePlayerSettingsScreen.TAG, singlePlayerSettingsScreen);
			screenMap.put(TwoPlayerSettingsScreen.TAG, twoPlayerSettingsScreen);
			screenMap.put(PlayScreen.TAG, playScreen);
			screenMap.put(OptionsScreen.TAG, optionsScreen);
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
		SettingsScreen.disposeAll();
		titleScreen = null;
		moreScreen = null;
		creditsScreen = null;
		singlePlayerSettingsScreen = null;
		twoPlayerSettingsScreen = null;
		levelsScreen = null;
		playScreen = null;
		optionsScreen = null;
		transitionScreen = null;
		screenMap = null;
	}
}
