package com.numbercortex;

import java.io.Reader;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class GameSettings {
	
	private int level;
	
	private int numberOfRows;
	private int time;
	
	private boolean evenOdd;
	private boolean singleDouble;
	private boolean primeComposite;
	private boolean middleExtreme;
	
	private boolean diagonals;
	private boolean fourSquare;
	
	private int difficulty;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}

	public boolean isEvenOdd() {
		return evenOdd;
	}
	public void setEvenOdd(boolean evenOdd) {
		this.evenOdd = evenOdd;
	}

	public boolean isSingleDouble() {
		return singleDouble;
	}
	public void setSingleDouble(boolean singleDouble) {
		this.singleDouble = singleDouble;
	}

	public boolean isPrimeComposite() {
		return primeComposite;
	}
	public void setPrimeComposite(boolean primeComposite) {
		this.primeComposite = primeComposite;
	}

	public boolean isMiddleExtreme() {
		return middleExtreme;
	}
	public void setMiddleExtreme(boolean middleExtreme) {
		this.middleExtreme = middleExtreme;
	}

	public boolean isDiagonals() {
		return diagonals;
	}
	public void setDiagonals(boolean diagonals) {
		this.diagonals = diagonals;
	}

	public boolean isFourSquare() {
		return fourSquare;
	}
	public void setFourSquare(boolean fourSquare) {
		this.fourSquare = fourSquare;
	}

	public int getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
}
