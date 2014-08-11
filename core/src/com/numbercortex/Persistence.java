package com.numbercortex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class Persistence {

    private static final String BACKGROUND_COLOR = "background_color";
    private static final String YOUR_NAME = "your_name";
    private static final String DOUBLE_TAP = "double_tap";
    private static final String SOUND = "sound";
    private static final String MUSIC = "music";
    private static final String TWO_PLAYER_CODE = "your_id";


    private static final String CURRENT_SCREEN = "currentScreen";
    private static final String MODE = "mode";
    private static final String IS_IN_PLAY = "isInPlay";
    private static final String CURRENT_CORTEX_STATE = "currentCortexState";

    private static final String CURRENT_LEVEL = "currentLevel";
    private static final String MAX_LEVEL = "maxLevel";

    private static final String PLAYER_ONE_NAME = "player_one_name";
    private static final String PLAYER_TWO_NAME = "player_two_name";
    private static final String TWO_PLAYER_GAME_SETTINGS = "twoPlayerGameSettings";
    
    private static final String LOSSES = "losses";
    private static final String TIES = "ties";
    private static final String MOVES = "moves";
    private static final String LOSSES_IN_A_ROW_AT_MAX_LEVEL = "losses_in_a_row_at_max_level";

    private boolean blue;
    private String yourName;
    private boolean doubleTap;
    private boolean sound;
    private boolean music;
    private String twoPlayerCode;

    private String currentScreen;
    private String mode;
    private boolean isInPlay;
    private CortexState currentCortexState;

    private int currentLevel;
    private int maxLevel;

    private String playerOneName;
    private String playerTwoName;
    private GameSettings twoPlayerGameSettings;
    
    private int losses;
    private int ties;
    private int moves;
    private int lossesInARowAtMaxLevel;
    
    private Preferences preferences;
    private static final String PREFERENCES_NAME = "preferences";

    private Persistence() {}
    private static class Singleton {
        final static Persistence instance = new Persistence();
    }
    public static Persistence getInstance() {
        return Singleton.instance;
    }

    public Persistence load() {
        preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
        loadStats();
        loadCusomizationPreferences();
        loadApplicationState();
        return Singleton.instance;
    }
    private void loadStats() {
        losses = preferences.getInteger(LOSSES, 0);
        ties = preferences.getInteger(TIES, 0);
        moves = preferences.getInteger(MOVES, 0);
        lossesInARowAtMaxLevel = preferences.getInteger(LOSSES_IN_A_ROW_AT_MAX_LEVEL, 0);
    }
    private void loadCusomizationPreferences() {
        blue = preferences.getBoolean(BACKGROUND_COLOR, true);
        yourName = preferences.getString(YOUR_NAME, "Player");
        doubleTap = preferences.getBoolean(DOUBLE_TAP, false);
        sound = preferences.getBoolean(SOUND, true);
        music = preferences.getBoolean(MUSIC, true);
        twoPlayerCode = preferences.getString(TWO_PLAYER_CODE, "27");
    }
    private void loadApplicationState() {
        loadGeneralSettings();
        loadSinglePlayerSettings();
        loadTwoPlayerSettings();
    }
    private void loadGeneralSettings() {
        currentScreen = preferences.getString(CURRENT_SCREEN, "");
        mode = preferences.getString(MODE, "");
        isInPlay = preferences.getBoolean(IS_IN_PLAY, false);
        loadCurrentCortexSettings();
    }
    private void loadCurrentCortexSettings() {
        String currentCortexStateJson = preferences.getString(CURRENT_CORTEX_STATE, "");
        if (!currentCortexStateJson.isEmpty()) {
            Json json = new Json();
            JsonValue root = new JsonReader().parse(currentCortexStateJson);
            currentCortexState = buildCurrentCortexState(json, root);
        }
    }
    private CortexState buildCurrentCortexState(Json json, JsonValue root) {
        String message = json.readValue("message", String.class, root);
        String currentPlayer = json.readValue("currentPlayer", String.class, root);
        ArrayList<String> players = json.readValue("players", ArrayList.class, String.class, root);
        int chosenNumber = json.readValue("chosenNumber", Integer.class, root);
        Map<Integer, Integer> checkedCoordinateNumberMap = readCheckedCoordinateNumberMap(json, root);
        ArrayList<Integer> availableNumbers = json.readValue("availableNumbers", ArrayList.class, Integer.class, root);
        CortexState currentCortexState;
        String winnerName = json.readValue("winner", String.class, root);
        if (winnerName != null) {
            String winningAttribute = json.readValue("winningAttribute", String.class, root);
            int[] winningCoordinates = json.readValue("winningValues", int[].class, root);
            currentCortexState = new CortexState.CortexStateBuilder(message, currentPlayer, players, chosenNumber, checkedCoordinateNumberMap, availableNumbers).win(winnerName, winningAttribute, winningCoordinates).build();
        } else {
            currentCortexState = new CortexState.CortexStateBuilder(message, currentPlayer, players, chosenNumber, checkedCoordinateNumberMap, availableNumbers).build();
        }
        return currentCortexState;
    }
    private Map<Integer, Integer> readCheckedCoordinateNumberMap(Json json, JsonValue root) {
        Map<Integer, Integer> uncheckedCoordinateNumberMap = json.readValue("coordinateNumberMap", Map.class, Integer.class, root);
        Map<Integer, Integer> checkedCoordinateNumberMap = new HashMap<Integer, Integer>();
        for (Map.Entry entry : uncheckedCoordinateNumberMap.entrySet()) {
            String key = (String) entry.getKey();
            Integer value = (Integer) entry.getValue();
            checkedCoordinateNumberMap.put(Integer.valueOf(key), value);
        }
        return checkedCoordinateNumberMap;
    }

    private void loadSinglePlayerSettings() {
        currentLevel = preferences.getInteger(CURRENT_LEVEL, 0);
        maxLevel = preferences.getInteger(MAX_LEVEL, 0);
    }
    private void loadTwoPlayerSettings() {
        playerOneName = preferences.getString(PLAYER_ONE_NAME, "One");
        playerTwoName = preferences.getString(PLAYER_TWO_NAME, "Two");
        loadTwoPlayerGameSettings();
    }
    private void loadTwoPlayerGameSettings() {
        Json json = new Json();
        String twoPlayerGameSettingsJson = preferences.getString(TWO_PLAYER_GAME_SETTINGS, "");
        if (!twoPlayerGameSettingsJson.isEmpty()) {
            twoPlayerGameSettings = json.fromJson(GameSettings.class, twoPlayerGameSettingsJson);
        } else {
            twoPlayerGameSettings = buildDefaultTwoPlayerGameSettings();
        }
    }
    private GameSettings buildDefaultTwoPlayerGameSettings() {
        GameSettings twoPlayerGameSettings = new GameSettings();
        twoPlayerGameSettings.setDiagonals(true);
        twoPlayerGameSettings.setEvenOdd(true);
        twoPlayerGameSettings.setFourSquare(false);
        twoPlayerGameSettings.setMiddleEdge(false);
        twoPlayerGameSettings.setNumberOfRows(4);
        twoPlayerGameSettings.setPrimeComposite(true);
        twoPlayerGameSettings.setSingleDouble(true);
        twoPlayerGameSettings.setTime(300);
        return twoPlayerGameSettings;
    }

    public void save() {
        saveStats();
        saveCustomizationPreferences();
        saveApplicationState();
        preferences.flush();
    }
    private void saveStats() {
        preferences.putInteger(LOSSES, losses);
        preferences.putInteger(TIES, ties);
        preferences.putInteger(MOVES, moves);
        preferences.putInteger(LOSSES_IN_A_ROW_AT_MAX_LEVEL, lossesInARowAtMaxLevel);
    }
    private void saveCustomizationPreferences() {
        preferences.putBoolean(BACKGROUND_COLOR, blue);
        preferences.putString(YOUR_NAME, yourName);
        preferences.putBoolean(DOUBLE_TAP, doubleTap);
        preferences.putBoolean(SOUND, sound);
        preferences.putBoolean(MUSIC, music);
        preferences.putString(TWO_PLAYER_CODE, twoPlayerCode);
    }
    private void saveApplicationState() {
        saveGeneralSettings();
        saveSinglePlayerSettings();
        saveTwoPlayerSettings();
    }
    private void saveGeneralSettings() {
        preferences.putString(CURRENT_SCREEN, currentScreen);
        preferences.putString(MODE, mode);
        preferences.putBoolean(IS_IN_PLAY, isInPlay);
        saveCurrentCortexState();
    }
    private void saveCurrentCortexState() {
        Json json = new Json();
        if (currentCortexState != null) {
            String cortexStateJson = json.toJson(currentCortexState, CortexState.class);
            preferences.putString(CURRENT_CORTEX_STATE, cortexStateJson);
        }
    }
    private void saveSinglePlayerSettings() {
        preferences.putInteger(CURRENT_LEVEL, currentLevel);
        preferences.putInteger(MAX_LEVEL, maxLevel);
    }
    private void saveTwoPlayerSettings() {
        preferences.putString(PLAYER_ONE_NAME, playerOneName);
        preferences.putString(PLAYER_TWO_NAME, playerTwoName);
        saveTwoPlayerGameSettings();
    }
    private void saveTwoPlayerGameSettings() {
        Json json = new Json();
        if (twoPlayerGameSettings != null) {
            String twoPlayerGameSettingsJson = json.toJson(twoPlayerGameSettings, GameSettings.class);
            preferences.putString(TWO_PLAYER_GAME_SETTINGS, twoPlayerGameSettingsJson);
        }
    }

    public boolean isBlue() {
        return blue;
    }
    public void setBlue(boolean blue) {
        this.blue = blue;
    }

    public String getYourName() {
        return yourName;
    }
    public void setYourName(String yourName) {
        this.yourName = yourName;
    }

    public boolean isDoubleTap() {
        return doubleTap;
    }
    public void setDoubleTap(boolean doubleTap) {
        this.doubleTap = doubleTap;
    }

    public boolean isSound() {
        return sound;
    }
    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public boolean isMusic() {
        return music;
    }
    public void setMusic(boolean music) {
        this.music = music;
    }
    
    public String getTwoPlayerCode() {
        return twoPlayerCode;
    }
    public void setTwoPlayerCode(String twoPlayerCode) {
        this.twoPlayerCode = twoPlayerCode;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public String getCurrentScreen() {
        return currentScreen;
    }
    public void setCurrentScreen(String currentScreen) {
        this.currentScreen = currentScreen;
    }

    public String getMode() {
        return mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isInPlay() {
        return isInPlay;
    }
    public void setInPlay(boolean isInPlay) {
        this.isInPlay = isInPlay;
    }

    public CortexState getCurrentCortexState() {
        return currentCortexState;
    }
    public void setCurrentCortexState(CortexState currentCortexState) {
        this.currentCortexState = currentCortexState;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }
    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }
    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }

    public GameSettings getTwoPlayerGameSettings() {
        return twoPlayerGameSettings;
    }
    public void setTwoPlayerGameSettings(GameSettings twoPlayerGameSettings) {
        this.twoPlayerGameSettings = twoPlayerGameSettings;
    }

    public int getLosses() {
        return losses;
    }
    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getTies() {
        return ties;
    }
    public void setTies(int ties) {
        this.ties = ties;
    }

    public int getMoves() {
        return moves;
    }
    public void setMoves(int moves) {
        this.moves = moves;
    }

    public int getLossesInARowAtMaxLevel() {
        return lossesInARowAtMaxLevel;
    }
    public void setLossesInARowAtMaxLevel(int lossesInARowAtMaxLevel) {
        this.lossesInARowAtMaxLevel = lossesInARowAtMaxLevel;
    }
}
