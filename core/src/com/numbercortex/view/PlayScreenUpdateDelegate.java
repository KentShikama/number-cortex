package com.numbercortex.view;

import java.util.ArrayList;
import java.util.Map;
import com.numbercortex.CortexState;
import com.numbercortex.logic.InteractableSendable;
import com.numbercortex.logic.Player;

class PlayScreenUpdateDelegate {

    private NumberCortexBoard board;
    private NumberScroller numberScroller;
    private MessageArea messageArea;
    private DragAndDropHandler handler = DragAndDropHandler.getInstance();

    PlayScreenUpdateDelegate(NumberCortexBoard board, NumberScroller numberScroller, MessageArea messageArea) {
        this.messageArea = messageArea;
        this.board = board;
        this.numberScroller = numberScroller;
    }

    void updateAll(Player currentPlayer, CortexState state) {
        updateCurrentPlayer(currentPlayer);
        updateChosenNumber(state);
        updateMessageArea(state);
        updateBoardMap(state);
        updateNumberScroller(state);        
    }
    void updateBoardMap(CortexState state) {
        Map<Integer, Integer> boardMap = state.getCoordinateNumberMap();
        for (Map.Entry<Integer, Integer> entry : boardMap.entrySet()) {
            int coordinate = entry.getKey();
            int number = entry.getValue();
            if (number != -1) {
                board.updateCell(coordinate, number);
            }
        }
    }

    private void updateCurrentPlayer(Player currentPlayer) {
        if (currentPlayer instanceof InteractableSendable) {
            InteractableSendable sendable = (InteractableSendable) currentPlayer;
            numberScroller.setSendable(sendable);
            handler.setSendable(sendable);
        } else {
            numberScroller.setSendable(null);
            handler.setSendable(null);
        }
    }
    private void updateChosenNumber(CortexState state) {
        int chosenNumber = state.getChosenNumber();
        if (chosenNumber != -1) {
            handler.setChosenNumber(chosenNumber);
        }
    }
    private void updateMessageArea(CortexState state) {
        String message = state.getMessage();
        int chosenNumber = state.getChosenNumber();
        if (chosenNumber != -1) {
            messageArea.updateMessageWithNextNumber(message, chosenNumber);
        } else {
            messageArea.updateMessage(message);
        }
    }
    private void updateNumberScroller(CortexState state) {
        ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
        numberScroller.update(availableNumbers);
    }
}
