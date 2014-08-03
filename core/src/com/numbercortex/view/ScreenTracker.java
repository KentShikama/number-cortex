package com.numbercortex.view;

import java.util.HashMap;
import java.util.Map;
import chartboost.CrossPlatformChartboost;

class ScreenTracker {

    private ScreenTracker() {}

    static TitleScreen titleScreen;
    static MoreScreen moreScreen;
    static CreditsScreen creditsScreen;
    static LevelsScreen levelsScreen;
    static SinglePlayerSettingsScreen singlePlayerSettingsScreen;
    static TwoPlayerSettingsScreen twoPlayerSettingsScreen;
    static PlayScreen playScreen;
    static OptionsScreen optionsScreen;

    static TransitionScreen transitionScreen;

    static void initializeScreens(Launch game, CrossPlatformChartboost chartboost) {
        titleScreen = new TitleScreen(game);
        moreScreen = new MoreScreen(game, chartboost);
        creditsScreen = new CreditsScreen(game);
        singlePlayerSettingsScreen = new SinglePlayerSettingsScreen(game);
        twoPlayerSettingsScreen = new TwoPlayerSettingsScreen(game);
        levelsScreen = new LevelsScreen(game);
        playScreen = new PlayScreen(game);
        optionsScreen = new OptionsScreen(game);

        transitionScreen = new TransitionScreen(game);
    }

    private static Map<String, GameScreen> screenMap;
    static GameScreen getScreen(String name) {
        if (screenMap == null) {
            screenMap = new HashMap<String, GameScreen>();
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
            GameScreen screen = screenMap.get(name);
            return screen;
        }
    }

    static void dispose() {
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
