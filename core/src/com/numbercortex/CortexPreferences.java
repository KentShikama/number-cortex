package com.numbercortex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class CortexPreferences {
	private static class Singleton {
		final static CortexPreferences instance = new CortexPreferences();
	}
	private Preferences preferences;

	private static final String PREFERENCES_NAME = "preferences";

	private static final String BACKGROUND_COLOR = "background_color";
	private static final String MUSIC = "music";
	private static final String CURRENT_LEVEL = "level";
	private static final String PLAYER_ONE_NAME = "player_one_name";
	private static final String PLAYER_TWO_NAME = "player_two_name";

	private static final String TIME = "time";
	private static final String NUMBER_OF_ROWS = "number_of_rows";
	private static final String EVEN_ODD = "even_odd";
	private static final String SINGLE_DOUBLE = "single_double";
	private static final String PRIME_COMPOSITE = "prime_composite";
	private static final String MIDDLE_EXTREME = "middle_extreme";
	private static final String DIAGONALS = "diagonals";
	private static final String FOUR_SQUARE = "four_square";
	private static final String DIFFICULTY = "difficulty";

	private boolean isBlue;
	private boolean isMusicEnabled;
	private int currentLevel;
	private String playerOneName;
	private String playerTwoName;
	private GameSettings twoPlayerGameSettings;

	private CortexPreferences() {}

	public static CortexPreferences getInstance() {
		return Singleton.instance;
	}

	public void load() {
		preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
		loadTwoPlayerGameSettings();
		loadOtherPreferences();
	}
	private void loadTwoPlayerGameSettings() {
		twoPlayerGameSettings = new GameSettings();
		int time = preferences.getInteger(TIME, 300);
		int numberOfRows = preferences.getInteger(NUMBER_OF_ROWS, 4);
		boolean isEvenOddEnabled = preferences.getBoolean(EVEN_ODD, true);
		boolean isSingleDoubleEnabled = preferences.getBoolean(SINGLE_DOUBLE, true);
		boolean isPrimeCompositeEnabled = preferences.getBoolean(PRIME_COMPOSITE, true);
		boolean isMiddleExtremeEnabled = preferences.getBoolean(MIDDLE_EXTREME, false);
		boolean isDiagonalsEnabled = preferences.getBoolean(DIAGONALS, true);
		boolean isFourSquareEnabled = preferences.getBoolean(FOUR_SQUARE, false);
		int difficulty = preferences.getInteger(DIFFICULTY, 3);
		twoPlayerGameSettings.setTime(time);
		twoPlayerGameSettings.setNumberOfRows(numberOfRows);
		twoPlayerGameSettings.setEvenOdd(isEvenOddEnabled);
		twoPlayerGameSettings.setSingleDouble(isSingleDoubleEnabled);
		twoPlayerGameSettings.setPrimeComposite(isPrimeCompositeEnabled);
		twoPlayerGameSettings.setMiddleExtreme(isMiddleExtremeEnabled);
		twoPlayerGameSettings.setDiagonals(isDiagonalsEnabled);
		twoPlayerGameSettings.setFourSquare(isFourSquareEnabled);
		twoPlayerGameSettings.setDifficulty(difficulty);
	}
	private void loadOtherPreferences() {
		isBlue = preferences.getBoolean(BACKGROUND_COLOR, true);
		isMusicEnabled = preferences.getBoolean(MUSIC, true);
		currentLevel = preferences.getInteger(CURRENT_LEVEL, 0);
		playerOneName = preferences.getString(PLAYER_ONE_NAME, "One");
		playerTwoName = preferences.getString(PLAYER_TWO_NAME, "Two");
	}

	public void save() {
		saveTwoPlayerGameSettings();
		saveOtherPreferences();
		preferences.flush();
	}
	private void saveOtherPreferences() {
		preferences.putBoolean(BACKGROUND_COLOR, isBlue);
		preferences.putBoolean(MUSIC, isMusicEnabled);
		preferences.putInteger(CURRENT_LEVEL, currentLevel);
		preferences.putString(PLAYER_ONE_NAME, playerOneName);
		preferences.putString(PLAYER_TWO_NAME, playerTwoName);
	}
	private void saveTwoPlayerGameSettings() {
		int time = twoPlayerGameSettings.getTime();
		int numberOfRows = twoPlayerGameSettings.getNumberOfRows();
		boolean isEvenOddEnabled = twoPlayerGameSettings.isEvenOdd();
		boolean isSingleDoubleEnabled = twoPlayerGameSettings.isSingleDouble();
		boolean isPrimeCompositeEnabled = twoPlayerGameSettings.isPrimeComposite();
		boolean isMiddleExtremeEnabled = twoPlayerGameSettings.isMiddleExtreme();
		boolean isDiagonalsEnabled = twoPlayerGameSettings.isDiagonals();
		boolean isFourSquareEnabled = twoPlayerGameSettings.isFourSquare();
		int difficulty = twoPlayerGameSettings.getDifficulty();

		preferences.putInteger(TIME, time);
		preferences.putInteger(NUMBER_OF_ROWS, numberOfRows);
		preferences.putBoolean(EVEN_ODD, isEvenOddEnabled);
		preferences.putBoolean(SINGLE_DOUBLE, isSingleDoubleEnabled);
		preferences.putBoolean(PRIME_COMPOSITE, isPrimeCompositeEnabled);
		preferences.putBoolean(MIDDLE_EXTREME, isMiddleExtremeEnabled);
		preferences.putBoolean(DIAGONALS, isDiagonalsEnabled);
		preferences.putBoolean(FOUR_SQUARE, isFourSquareEnabled);
		preferences.putInteger(DIFFICULTY, difficulty);
	}

	public boolean isBlue() {
		return isBlue;
	}
	public void setBlue(boolean isBlue) {
		this.isBlue = isBlue;
	}

	public boolean isMusicEnabled() {
		return isMusicEnabled;
	}
	public void setMusicEnabled(boolean isMusicEnabled) {
		this.isMusicEnabled = isMusicEnabled;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}
	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
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

}
