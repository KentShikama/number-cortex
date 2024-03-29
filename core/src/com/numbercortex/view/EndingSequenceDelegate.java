package com.numbercortex.view;

import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.numbercortex.CortexState;
import com.numbercortex.logic.Player;

class EndingSequenceDelegate {

    private PlayScreenControls controls;

    EndingSequenceDelegate(PlayScreenControls controls) {
        this.controls = controls;
    }

    void animateEndingSequence(CortexState state, Player currentPlayer, Action signifyEndAction) {
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
        controls.getMessageArea().showEndingMessageSequenceWithAnimation(winner, winningAttribute, currentAnimationTime, signifyEndAction);
    }
    private float handleShowingOfWinningCoordinates(CortexState state) {
        int[] winningValues = state.getWinningValues();
        Map<Integer, Integer> winningMap = buildWinningMap(state, winningValues);
        return controls.getBoard().showWinningCoordinates(winningMap);
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
        controls.getBoard().bringCellsDownWithAnimation(delay);
        removeOtherElementsWithAnimation(delay);
        return 1f;
    }
    private void removeOtherElementsWithAnimation(float delay) {
        controls.getNumberScroller().removeScrollerWithAnimation(delay);
        controls.getExitButton().clearListeners();
        AnimationUtilities.delayFadeAndRemoveActor(controls.getExitButton(), delay);
        if (!isTutorialLevelEnd()) {
        	controls.getRestartButton().clearListeners();
        	controls.getInformationButton().clearListeners();
        	controls.getOptionsButton().clearListeners();
        	AnimationUtilities.delayFadeAndRemoveActor(controls.getRestartButton(), delay);
        	AnimationUtilities.delayFadeAndRemoveActor(controls.getInformationButton(), delay);
        	AnimationUtilities.delayFadeAndRemoveActor(controls.getOptionsButton(), delay);
        }
    }
	private boolean isTutorialLevelEnd() {
		return controls.getRestartButton() == null && controls.getInformationButton() == null && controls.getOptionsButton() == null;
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
        controls.getMessageArea().showEndingMessageSequence(winner);
    }
    private void moveDownBoardAndRemoveOtherElements() {
        controls.getBoard().bringCellsDown();
        controls.getNumberScroller().removeScroller();
        controls.getExitButton().remove();
        controls.getRestartButton().remove();
        controls.getInformationButton().remove();
        controls.getOptionsButton().remove();
    }
}
