package com.numbercortex.view;

import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.numbercortex.CortexState;
import com.numbercortex.logic.Player;

class EndingSequenceDelegate {
    
    private NumberCortexBoard board;
    private NumberScroller numberScroller;
    private MessageArea messageArea;
    private Image exitButton;
    private Image informationButton;
    private Image optionsButton;
    
    EndingSequenceDelegate(NumberCortexBoard board, NumberScroller numberScroller, MessageArea messageArea, Image exitButton, Image informationButton, Image optionsButton) {
        this.messageArea = messageArea;
        this.board = board;
        this.numberScroller = numberScroller;
        this.exitButton = exitButton;
        this.informationButton = informationButton;
        this.optionsButton = optionsButton;
    }
    
    void animateEndingSequence(CortexState state, Player currentPlayer) {
        String winningAttribute = state.getWinningAttribute();
        float currentAnimationTime = 0f;
        Player winner;
        if (winningAttribute != null) {
            currentAnimationTime += handleShowingOfWinningCoordinates(state);
            winner = currentPlayer;
        } else {
            int tieDelay = 1;
            currentAnimationTime += tieDelay;
            winner = null;
        }
        currentAnimationTime += moveDownBoardAndRemoveOtherElements(currentAnimationTime);
        messageArea.showEndingMessageSequenceWithAnimation(winner, winningAttribute, currentAnimationTime);
    }
    private float handleShowingOfWinningCoordinates(CortexState state) {
        int[] winningValues = state.getWinningValues();
        Map<Integer, Integer> winningMap = buildWinningMap(state, winningValues);
        return board.showWinningCoordinates(winningMap);
    }
    private Map<Integer, Integer> buildWinningMap(CortexState state, int[] winningValues) {
        Map<Integer, Integer> winningMap = new HashMap<Integer, Integer>();
        for (Map.Entry<Integer, Integer> entry : state.getCoordinateNumberMap().entrySet()) {
            for (Integer winningValue : winningValues) {
                if (entry.getValue() == winningValue) {
                    int winningCoordinate = entry.getKey();
                    winningMap.put(winningCoordinate, winningValue);
                }
            }
        }
        return winningMap;
    }
    private float moveDownBoardAndRemoveOtherElements(float delay) {
        board.bringCellsDownWithAnimation(delay);
        removeOtherElementsWithAnimation(delay);
        return 1f;
    }
    private void removeOtherElementsWithAnimation(float delay) {
        numberScroller.removeScrollerWithAnimation(delay);
        exitButton.clearListeners();
        informationButton.clearListeners();
        optionsButton.clearListeners();
        AnimationUtilities.delayFadeAndRemoveActor(exitButton, delay);
        AnimationUtilities.delayFadeAndRemoveActor(informationButton, delay);
        AnimationUtilities.delayFadeAndRemoveActor(optionsButton, delay);
    }
    
    void recreateEndingInstantly(CortexState state, Player currentPlayer) {
        String winningAttribute = state.getWinningAttribute();
        moveDownBoardAndRemoveOtherElements();
        Player winner;
        if (winningAttribute != null) {
            winner = currentPlayer;
        } else {
            winner = null;
        }
        messageArea.showEndingMessageSequence(winner);
    }
    private void moveDownBoardAndRemoveOtherElements() {
        board.bringCellsDown();
        numberScroller.removeScroller();
        exitButton.remove();
        informationButton.remove();
        optionsButton.remove();
    }
}
