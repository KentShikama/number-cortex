package com.numbercortex.view;

import libgdx.NumberTextButton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.numbercortex.ModeTracker;
import com.numbercortex.Persistence;
import com.numbercortex.logic.InteractableSendable;
import com.numbercortex.logic.Player;
import com.numbercortex.view.TransitionScreen.Direction;

class MessageArea {

    static final String TAG = "Message Area";
    static final String NEXT_NUMBER_SQUARE_NAME = "16";

    private static final String CONTINUE = "continue";
    private static final String REPLAY = "replay";

    private static final int MAXIMUM_POSSIBLE_LEVEL = 18;

    private static TextButtonStyle textButtonStyle = buildBorderlessTextButtonStyle();
    private static TextButtonStyle buildBorderlessTextButtonStyle() {
        BitmapFont font = Assets.gillSans57;
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.WHITE;
        return textButtonStyle;
    }

    private static final String TEXT_BUTTON_BORDER_TEXTURE_NAME = "white_button";
    private static TextButton.TextButtonStyle borderedTextButtonStyle = buildBorderedTextButtonStyle();
    private static TextButton.TextButtonStyle buildBorderedTextButtonStyle() {
        BitmapFont font = Assets.gillSans57;
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.up = Assets.dialogSkin.getDrawable(TEXT_BUTTON_BORDER_TEXTURE_NAME);
        return textButtonStyle;
    }

    private static final String NEXT_NUMBER_TEXTURE_NAME = "next_number";
    private static NumberTextButton.NumberTextButtonStyle nextNumberStyle = buildNextNumberStyle(NEXT_NUMBER_TEXTURE_NAME);
    private static NumberTextButton.NumberTextButtonStyle buildNextNumberStyle(String textureName) {
        BitmapFont font = Assets.boardNumberFont;
        Drawable numberRectangle = Assets.gameSkin.getDrawable(textureName);
        NumberTextButton.NumberTextButtonStyle buttonStyle = new NumberTextButton.NumberTextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Launch.BRIGHT_YELLOW;
        buttonStyle.up = numberRectangle;
        return buttonStyle;
    }

    private Stage stage;
    private TextButton messageLabelShort;
    private TextButton messageLabelLong;
    private NumberTextButton nextNumberSquare;

    private Table buttonTable = new Table();
    private TextButton menuButton;
    private TextButton playButton;
    private TextButton continueButton;

    private static class Singleton {
        private static final MessageArea INSTANCE = new MessageArea();
    }

    private MessageArea() {}

    static MessageArea createMessageArea(Stage stage) {
        float worldWidth = stage.getViewport().getWorldWidth();
        MessageArea instance = Singleton.INSTANCE;
        instance.stage = stage;
        instance.messageLabelLong = buildMessageLabelLong(worldWidth);
        instance.messageLabelShort = buildMessageLabelShort(worldWidth);
        instance.nextNumberSquare = buildNextNumberSquare(worldWidth);
        instance.buttonTable = buildButtonTable(worldWidth);
        instance.menuButton = buildMenuButton();
        instance.playButton = buildPlayButton();
        instance.continueButton = buildContinueButton();
        return instance;
    }
    private static TextButton buildMessageLabelLong(float worldWidth) {
        if (textButtonStyle == null) {
            textButtonStyle = buildBorderlessTextButtonStyle();
        }
        TextButton messageLabelLong = new TextButton("", textButtonStyle);
        int paddingFromEdge = 30;
        float offsetFromOriginalWidth = (worldWidth - Launch.SCREEN_WIDTH) / 2;
        messageLabelLong.setBounds(paddingFromEdge + offsetFromOriginalWidth, Launch.SCREEN_HEIGHT - 175, Launch.SCREEN_WIDTH - paddingFromEdge * 2, 145);
        messageLabelLong.getLabel().setWrap(true);
        return messageLabelLong;
    }
    private static TextButton buildMessageLabelShort(float worldWidth) {
        if (textButtonStyle == null) {
            textButtonStyle = buildBorderlessTextButtonStyle();
        }
        TextButton messageLabelShort = new TextButton("", textButtonStyle);
        int paddingFromEdge = 30;
        float offsetFromOriginalWidth = (worldWidth - Launch.SCREEN_WIDTH) / 2;
        messageLabelShort.setBounds(paddingFromEdge + offsetFromOriginalWidth, Launch.SCREEN_HEIGHT - 175, Launch.SCREEN_WIDTH - paddingFromEdge * 2 - 165, 145);
        messageLabelShort.getLabel().setWrap(true);
        return messageLabelShort;
    }
    private static NumberTextButton buildNextNumberSquare(float worldWidth) {
        if (nextNumberStyle == null) {
            nextNumberStyle = buildNextNumberStyle(NEXT_NUMBER_TEXTURE_NAME);
        }
        NumberTextButton nextNumberSquare = new NumberTextButton("", nextNumberStyle);
        int offsetFromOriginalWidth = (int) ((worldWidth - Launch.SCREEN_WIDTH) / 2);
        nextNumberSquare.setBounds(worldWidth - 165 - offsetFromOriginalWidth, Launch.SCREEN_HEIGHT - 175, 141, 141);
        nextNumberSquare.setName(NEXT_NUMBER_SQUARE_NAME);
        return nextNumberSquare;
    }
    private static Table buildButtonTable(float worldWidth) {
        Table buttonTable = new Table();
        int paddingFromEdge = 30;
        buttonTable.setBounds(paddingFromEdge, Launch.SCREEN_HEIGHT - 370, worldWidth - paddingFromEdge * 2, 145);
        return buttonTable;
    }
    private static TextButton buildMenuButton() {
        if (borderedTextButtonStyle == null) {
            borderedTextButtonStyle = buildBorderedTextButtonStyle();
        }
        TextButton menuButton = new TextButton("Menu", borderedTextButtonStyle);
        menuButton.addListener(new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenTracker.transitionScreen.transition(Direction.LEFT, ScreenTracker.titleScreen);
            }
        });
        return menuButton;
    }
    private static TextButton buildPlayButton() {
        if (borderedTextButtonStyle == null) {
            borderedTextButtonStyle = buildBorderedTextButtonStyle();
        }
        TextButton playButton = new TextButton("Play", borderedTextButtonStyle);
        playButton.addListener(new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (ModeTracker.mode == ModeTracker.Mode.SINGLE_PLAYER) {
                    ScreenTracker.transitionScreen.transition(Direction.LEFT, ScreenTracker.singlePlayerSettingsScreen);
                } else {
                    ScreenTracker.transitionScreen.transition(Direction.LEFT, ScreenTracker.twoPlayerSettingsScreen);
                }
            }
        });
        return playButton;
    }
    private static TextButton buildContinueButton() {
        if (borderedTextButtonStyle == null) {
            borderedTextButtonStyle = buildBorderedTextButtonStyle();
        }
        TextButton continueButton = new TextButton("Continue", borderedTextButtonStyle);
        continueButton.addListener(new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Persistence persistence = Persistence.getInstance();
                int currentLevel = persistence.getCurrentLevel();
                int nextLevel = currentLevel + 1;
                persistence.setCurrentLevel(nextLevel);
                ScreenTracker.transitionScreen.transition(Direction.LEFT, ScreenTracker.singlePlayerSettingsScreen);
            }
        });
        return continueButton;
    }

    NumberTextButton getNextNumberSquare() {
        return nextNumberSquare;
    }

    void updateMessage(String message) {
        messageLabelLong.setText(message);
        messageLabelShort.remove();
        nextNumberSquare.remove();
        buttonTable.remove();
        stage.addActor(messageLabelLong);
    }

    void updateMessageWithNextNumber(String message, int nextNumber) {
        Label newLabel = buildNumberLabel();
        nextNumberSquare.setLabel(newLabel);
        nextNumberSquare.setText(String.valueOf(nextNumber));
        messageLabelShort.setText(message);
        messageLabelLong.remove();
        buttonTable.remove();
        stage.addActor(messageLabelShort);
        stage.addActor(nextNumberSquare);
    }
    private static Label buildNumberLabel() {
        BitmapFont font = Assets.boardNumberFont;
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Launch.BRIGHT_YELLOW;
        Label label = new Label("", labelStyle);
        return label;
    }

    void flashChosenNumber(int chosenNumber) {
        RepeatAction repeatAction = buildRepeatableFlashingAction(chosenNumber);
        nextNumberSquare.addAction(repeatAction);
    }
    private RepeatAction buildRepeatableFlashingAction(int chosenNumber) {
        DelayAction delayAction = Actions.delay(0.3f);
        Action toggleAction = buildToggleAction(chosenNumber);
        SequenceAction sequence = Actions.sequence(toggleAction, delayAction);
        RepeatAction repeatAction = new RepeatAction();
        repeatAction.setAction(sequence);
        repeatAction.setCount(4);
        return repeatAction;
    }
    private Action buildToggleAction(final int chosenNumber) {
        Action toggleAction = new Action() {
            @Override
            public boolean act(float delta) {
                toggleCell();
                return true;
            }
            private void toggleCell() {
                if (nextNumberSquare.isHighlighted()) {
                    nextNumberSquare.setText(String.valueOf(chosenNumber));
                    nextNumberSquare.setHighlighted(false);
                } else {
                    nextNumberSquare.setText("");
                    nextNumberSquare.setHighlighted(true);
                }
            }
        };
        return toggleAction;
    }

    void showEndingMessageSequence(final Player winner) {
        updateMessage("");
        Action showNextOptions = buildShowNextOptionsAction(winner);
        stage.addAction(showNextOptions);
    }
    void showEndingMessageSequenceWithAnimation(final Player winner, final String winningAttribute, float delay) {
        Action showWinner = buildShowWinnerAction(winner, winningAttribute);
        DelayAction delayAction = Actions.delay(delay);
        Action showNextOptions = buildShowNextOptionsAction(winner);
        stage.addAction(Actions.sequence(showWinner, delayAction, showNextOptions));
    }
    private Action buildShowWinnerAction(final Player winner, final String winningAttribute) {
        Action showWinner = new Action() {
            @Override
            public boolean act(float delta) {
                if (winner == null) {
                    String tieMessage = "Tie game!";
                    updateMessage(tieMessage);
                } else {
                    String winnerName = winner.getName();
                    String winningMessage = winnerName + " wins!\n" + "(" + winningAttribute + ")";
                    updateMessage(winningMessage);
                }
                return true;
            }
        };
        return showWinner;
    }
    private Action buildShowNextOptionsAction(final Player winner) {
        Action showNextOptions = new Action() {
            @Override
            public boolean act(float delta) {
                if (winner != null && ModeTracker.mode == ModeTracker.Mode.SINGLE_PLAYER && winner instanceof InteractableSendable && Persistence.getInstance().getCurrentLevel() != MAXIMUM_POSSIBLE_LEVEL) {
                    updateMessageWithButtons(CONTINUE);
                } else {
                    updateMessageWithButtons(REPLAY);
                }
                return true;
            }
        };
        return showNextOptions;
    }
    private void updateMessageWithButtons(String message) {
        messageLabelLong.setY(Launch.SCREEN_HEIGHT - 225);
        buttonTable.clear();
        buttonTable.add(menuButton).pad(20).padTop(50);
        stage.addActor(buttonTable);
        if (message.equals(REPLAY)) {
            messageLabelLong.setText("Do you wish to play again?");
            buttonTable.add(playButton).width(300).pad(15).padTop(50);
        } else if (message.equals(CONTINUE)) {
            messageLabelLong.setText("Do you wish to continue to the next level?");
            buttonTable.add(continueButton).width(300).pad(15).padTop(50);
        } else {
            Gdx.app.log(TAG, "Impossible update message.");
        }
    }

    static void dispose() {
        textButtonStyle = null;
        borderedTextButtonStyle = null;
        nextNumberStyle = null;
    }
}
