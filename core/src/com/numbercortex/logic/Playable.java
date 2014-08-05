package com.numbercortex.logic;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.numbercortex.CortexState;
import com.numbercortex.GameSettings;
import com.numbercortex.Persistence;

public interface Playable {
    void setGameSettingsAndPreferences(GameSettings settings, Persistence preferences);
    void updateState(CortexState state, Player player);

    void showConfirmationDialog(String... dialogMessages);
    void showConfirmationDialog(float delay, final String... dialogMessages);
    void generateShareDialog(float delay, String dialogMessage, String facebookPostTitle, String facebookPostDescription);

    void flashChosenNumber(int chosenNumber);
    void placeNumberWithAnimation(int coordinate, Action completePlaceNumberAction);
    void chooseNumberWithAnimation(int nextNumber, Action completeChooseNumberAction);
}
