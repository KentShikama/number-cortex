package com.numbercortex.view;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

class PlayScreenControls {

    private NumberCortexBoard board;
    private NumberScroller numberScroller;
    private MessageArea messageArea;
    private Image exitButton;
    private Image restartButton;
    private Image informationButton;
    private Image optionsButton;

    NumberCortexBoard getBoard() {
        return board;
    }
    void setBoard(NumberCortexBoard board) {
        this.board = board;
    }
    NumberScroller getNumberScroller() {
        return numberScroller;
    }
    void setNumberScroller(NumberScroller numberScroller) {
        this.numberScroller = numberScroller;
    }
    MessageArea getMessageArea() {
        return messageArea;
    }
    void setMessageArea(MessageArea messageArea) {
        this.messageArea = messageArea;
    }
    Image getExitButton() {
        return exitButton;
    }
    void setExitButton(Image exitButton) {
        this.exitButton = exitButton;
    }
    Image getRestartButton() {
    	return restartButton;
    }
    void setRestartButton(Image restartButton) {
    	this.restartButton = restartButton;
    }
    Image getInformationButton() {
        return informationButton;
    }
    void setInformationButton(Image informationButton) {
        this.informationButton = informationButton;
    }
    Image getOptionsButton() {
        return optionsButton;
    }
    void setOptionsButton(Image optionsButton) {
        this.optionsButton = optionsButton;
    }
}
