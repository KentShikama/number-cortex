package com.numbercortex.view;

import java.util.ArrayList;
import java.util.Map;
import libgdx.NumberTextButton;
import libgdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.numbercortex.CortexState;
import com.numbercortex.GameSettings;
import com.numbercortex.ModeTracker;
import com.numbercortex.Persistence;
import com.numbercortex.logic.BoardUtilities;
import com.numbercortex.logic.GameManager;
import com.numbercortex.logic.InteractableSendable;
import com.numbercortex.logic.Playable;
import com.numbercortex.logic.Player;
import com.numbercortex.logic.SinglePlayerGameManager;
import com.numbercortex.logic.TwoPlayerGameManager;
import com.numbercortex.view.TransitionScreen.Direction;
import facebook.CrossPlatformFacebook;

class PlayScreen extends GameScreen implements Playable {

    static final String TAG = "Play Screen";

    private NumberCortexBoard board;
    private NumberScroller numberScroller;
    private MessageArea messageArea;
    private Image exitButton;
    private Image informationButton;
    private Image optionsButton;

    private boolean isShown;

    private PlayScreenDialogDelegate dialogDelegate;
    private PlayScreenBuildDelegate buildDelegate;
    private PlayScreenUpdateDelegate updateDelegate;
    private EndingSequenceDelegate endingSequenceDelegate;

    PlayScreen(Game game, CrossPlatformFacebook facebook) {
        super(game);
        ExtendViewport fitViewport = new ExtendViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT, (float) (Launch.SCREEN_HEIGHT / 1.2), Launch.SCREEN_HEIGHT);
        stage = new Stage(fitViewport);
        buildDelegate = new PlayScreenBuildDelegate(stage, board, numberScroller, messageArea, exitButton, informationButton, optionsButton);
        updateDelegate = new PlayScreenUpdateDelegate(board, numberScroller, messageArea);
        endingSequenceDelegate = new EndingSequenceDelegate(board, numberScroller, messageArea, exitButton, informationButton, optionsButton);
        dialogDelegate = new PlayScreenDialogDelegate(facebook);
    }

    @Override
    public void setGameSettingsAndPreferences(GameSettings settings, Persistence preferences) {
        buildDelegate.setGameSettingsAndPreferences(settings, preferences);
    }

    @Override
    public void show() {
        if (isShown) {
            return;
        } else {
            isShown = true;
            setUpInputAndBackKey();
            buildDelegate.buildPlayScreenStage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            resumeGame();
        }
    }
    @Override
    public void hide() {
        isShown = false;
    }
    private void setUpInputAndBackKey() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
        backKey = false;
    }
    private void resumeGame() {
        GameManager gameManager = getGameManagerInstance();
        gameManager.resumeGame();
    }

    @Override
    public void updateState(CortexState state, Player currentPlayer) {
        final String winner = state.getWinner();
        Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
        ArrayList<Integer> openCoordinates = BoardUtilities.getOpenCoordinates(coordinateNumberMap);
        if (winner == null && !openCoordinates.isEmpty()) {
            updateDelegate.updateAll(currentPlayer, state);
        } else {
            updateDelegate.updateBoardMap(state);
            if (Persistence.getInstance().isInPlay()) {
                endingSequenceDelegate.animateEndingSequence(state, currentPlayer);
                Persistence.getInstance().setInPlay(false);
            } else {
                endingSequenceDelegate.recreateEndingInstantly(state, currentPlayer);
            }
        }
    }

    @Override
    public void showTutorialDialogs(String... dialogMessages) {
        Dialog tutorialDialogs = CortexDialog.createConfirmationDialogs(stage, dialogMessages);
        tutorialDialogs.show(stage);
    }
    @Override
    public void generateConfirmationDialog(float delay, final String dialogMessages) {
        dialogDelegate.generateConfirmationDialog(stage, delay, dialogMessages);
    }
    @Override
    public void generateShareDialog(float delay, final String dialogMessage, final String facebookPostTitle, final String facebookPostDescription) {
        dialogDelegate.generateShareDialogWithDelayIfApplicable(stage, delay, dialogMessage, facebookPostTitle, facebookPostDescription);
    }

    @Override
    public void flashChosenNumber(int chosenNumber) {
        messageArea.flashChosenNumber(chosenNumber);
    }
    
    @Override
    public void placeNumberWithAnimation(int coordinate, Action completePlaceNumberAction) {
        NumberTextButton nextNumberCell = messageArea.getNextNumberSquare();
        MoveToAction moveToAction = buildMoveToAction(coordinate, nextNumberCell);
        DelayAction delayAction = Actions.delay(0.5f);
        SequenceAction placeNumberAction = Actions.sequence(delayAction, moveToAction, completePlaceNumberAction);
        Label nextNumberLabel = nextNumberCell.getLabel();
        nextNumberLabel.addAction(placeNumberAction);
    }
    private MoveToAction buildMoveToAction(int coordinate, NumberTextButton nextNumberCell) {
        int numberOfRows = board.getNumberOfRows();
        int offset = numberOfRows == 3 ? 36 : 10;
        float nextNumberLabelX = nextNumberCell.getX() - offset;
        float nextNumberLabelY = nextNumberCell.getY() - offset;
        float dragToPositionX = board.getBoardCells().get(coordinate).getX();
        float dragToPositionY = board.getBoardCells().get(coordinate).getY();
        MoveToAction moveToAction = Actions.moveTo(dragToPositionX - nextNumberLabelX, dragToPositionY - nextNumberLabelY, 0.7f);
        return moveToAction;
    }
    @Override
    public void chooseNumberWithAnimation(int nextNumber, Action completeChooseNumberAction) {
        numberScroller.chooseNumberWithAnimation(nextNumber, completeChooseNumberAction);
    }

    @Override
    public void resize(int width, int height) {
        buildDelegate.buildPlayScreenStage(width, height);
        resumeGame();
    }
    @Override
    public void render(float delta) {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        stage.act(delta);
        stage.draw();
        dialogDelegate.draw(stage);
        handleBackKey();
    }
    private void handleBackKey() {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            backKey = true;
        } else if (backKey) {
            backKey = false;
            handleScreenSwitch();
        }
    }
    private void handleScreenSwitch() {
        boolean dialogAlreadyExists = checkIfDialogAlreadyIsShownOnStage();
        final Persistence persistence = Persistence.getInstance();
        if (!persistence.isInPlay()) {
            ScreenTracker.transitionScreen.transition(Direction.LEFT, ScreenTracker.titleScreen);
        } else if (!dialogAlreadyExists) {
            Dialog dialog = CortexDialog.createGameQuitCancelDialog();
            dialog.show(stage);
        }
    }
    private boolean checkIfDialogAlreadyIsShownOnStage() {
        boolean dialogAlreadyExists = false;
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Dialog) {
                dialogAlreadyExists = true;
            }
        }
        return dialogAlreadyExists;
    }
    
    @Override
    public void pause() {
        Persistence persistence = Persistence.getInstance();
        persistence.setCurrentScreen(TAG);
        GameManager gameManager = getGameManagerInstance();
        CortexState currentState = gameManager.getState();
        persistence.setCurrentCortexState(currentState);
        persistence.setMode(ModeTracker.mode.name());
    }
    private GameManager getGameManagerInstance() {
        GameManager gameManager;
        if (ModeTracker.mode == ModeTracker.Mode.SINGLE_PLAYER) {
            gameManager = SinglePlayerGameManager.getInstance();
        } else {
            gameManager = TwoPlayerGameManager.getInstance();
        }
        return gameManager;
    }
    
    @Override
    public void dispose() {
        MessageArea.dispose();
        NumberCortexBoard.dispose();
        NumberScroller.dispose();
    }
}
