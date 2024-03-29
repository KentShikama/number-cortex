package com.numbercortex;

import java.io.Serializable;

public class GameSettings implements Serializable {

    private int level;

    private int numberOfRows;
    private int time;

    private boolean evenOdd;
    private boolean singleDouble;
    private boolean primeComposite;
    private boolean middleEdge;

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

    public boolean isMiddleEdge() {
        return middleEdge;
    }
    public void setMiddleEdge(boolean middleEdge) {
        this.middleEdge = middleEdge;
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
