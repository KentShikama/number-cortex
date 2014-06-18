package com.numbercortex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class CortexPreferences {
	private static class Singleton {
		final static CortexPreferences instance = new CortexPreferences();
	}
	
	private CortexPreferences() {}
	
	public static CortexPreferences getInstance() {
		return Singleton.instance;
	}
	
	private Preferences preferences;
	private static final String PREFERENCES_NAME = "preferences";
	
	private boolean isBlue;
	private boolean isDiagonalsEnabled;
	private boolean isFourSquareEnabled;
	private boolean isMusicEnabled;
	private int difficulty;
	
	private static final String BACKGROUND_COLOR = "background_color";
	private static final String DIAGONALS = "diagonals";
	private static final String FOUR_SQUARE = "four_square";
	private static final String MUSIC = "music";
	private static final String DIFFICULTY = "difficulty";

	public void load() {
		preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
		isBlue = preferences.getBoolean(BACKGROUND_COLOR, true);
		isDiagonalsEnabled = preferences.getBoolean(DIAGONALS, true);
		isFourSquareEnabled = preferences.getBoolean(FOUR_SQUARE, false);
		isMusicEnabled = preferences.getBoolean(MUSIC, true);
		difficulty = preferences.getInteger(DIFFICULTY, 2);
	}
	
	public void save() {
		preferences.putBoolean(BACKGROUND_COLOR, isBlue);
		preferences.putBoolean(DIAGONALS, isDiagonalsEnabled);
		preferences.putBoolean(FOUR_SQUARE, isFourSquareEnabled);
		preferences.putBoolean(MUSIC, isMusicEnabled);
		preferences.putInteger(DIFFICULTY, difficulty);
		preferences.flush();
	}

	public boolean isBlue() {
		return isBlue;
	}

	public void setBlue(boolean isBlue) {
		this.isBlue = isBlue;
	}

	public boolean isDiagonalsEnabled() {
		return isDiagonalsEnabled;
	}

	public void setDiagonalsEnabled(boolean isDiagonalsEnabled) {
		this.isDiagonalsEnabled = isDiagonalsEnabled;
	}

	public boolean isFourSquareEnabled() {
		return isFourSquareEnabled;
	}

	public void setFourSquareEnabled(boolean isFourSquareEnabled) {
		this.isFourSquareEnabled = isFourSquareEnabled;
	}

	public boolean isMusicEnabled() {
		return isMusicEnabled;
	}

	public void setMusicEnabled(boolean isMusicEnabled) {
		this.isMusicEnabled = isMusicEnabled;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
}
