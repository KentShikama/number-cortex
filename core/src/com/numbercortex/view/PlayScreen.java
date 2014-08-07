package com.numbercortex.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import libgdx.NumberTextButton;
import libgdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
    
    private DragAndDropHandler handler = DragAndDropHandler.getInstance();

    private GameSettings settings;
    private Persistence preferences;

    private boolean isShown;

    private PlayScreenDialogDelegate dialogDelegate;
    private EndingSequenceDelegate endingSequenceDelegate;

    PlayScreen(Game game, CrossPlatformFacebook facebook) {
        super(game);
        ExtendViewport fitViewport = new ExtendViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT, (float) (Launch.SCREEN_HEIGHT / 1.2), Launch.SCREEN_HEIGHT);
        stage = new Stage(fitViewport);
        dialogDelegate = new PlayScreenDialogDelegate(facebook);
        endingSequenceDelegate = new EndingSequenceDelegate(board, numberScroller, messageArea, exitButton, informationButton, optionsButton);
    }

    @Override
    public void setGameSettingsAndPreferences(GameSettings settings, Persistence preferences) {
        this.settings = settings;
        this.preferences = preferences;
    }
    @Override
    public void show() {
        if (isShown) {
            return;
        } else {
            isShown = true;
            setUpInputAndBackKey();
            buildPlayScreenStage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
    private void buildPlayScreenStage(int width, int height) {
        stage.getViewport().update(width, height, true);
        stage.clear();
        buildMessageArea(game);
        buildBoard(settings, preferences);
        buildNumberScroller();
        buildBottomButtons();
        board.clearBoard();
    }
    private void buildMessageArea(Game game) {
        messageArea = MessageArea.createMessageArea(stage, game);
        handler.notifyMessageAreaConstrucion(messageArea);
    }
    private void buildBoard(GameSettings settings, Persistence preferences) {
        boolean isBlue = preferences.isBlue();
        int numberOfRows = settings.getNumberOfRows();
        board = NumberCortexBoard.createNumberCortexBoard(stage, isBlue, numberOfRows);
        handler.notifyBoardConstruction(board);
    }
    private void buildNumberScroller() {
        numberScroller = NumberScroller.createNumberScroller(stage);
    }
    private void buildBottomButtons() {
        TextureRegion exitRectangleTexture = Assets.gameSkin.getRegion("exit");
        TextureRegion informationRectangleTexture = Assets.gameSkin.getRegion("information");
        TextureRegion optionsRectangleTexture = Assets.gameSkin.getRegion("options");
        float worldWidth = stage.getViewport().getWorldWidth();
        float offsetFromOriginalWidth = (worldWidth - Launch.SCREEN_WIDTH) / 2;
        bulidExitButton(exitRectangleTexture, offsetFromOriginalWidth);
        buildInformationButton(informationRectangleTexture, offsetFromOriginalWidth);
        buildHelpButton(optionsRectangleTexture, offsetFromOriginalWidth);
    }
    private void bulidExitButton(TextureRegion exitRectangleTexture, float offsetFromOriginalWidth) {
        exitButton = new Image(exitRectangleTexture);
        exitButton.setBounds(44 + offsetFromOriginalWidth, Launch.SCREEN_HEIGHT - 1136, exitRectangleTexture.getRegionWidth(), exitRectangleTexture.getRegionHeight());
        exitButton.addListener(new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Dialog dialog = buildQuitCancelDialog();
                dialog.show(stage);
            }
        });
        stage.addActor(exitButton);
    }
    private void buildInformationButton(TextureRegion informationRectangleTexture, float offsetFromOriginalWidth) {
        informationButton = new Image(informationRectangleTexture);
        informationButton.setBounds(434 + offsetFromOriginalWidth, Launch.SCREEN_HEIGHT - 1136, informationRectangleTexture.getRegionWidth(), informationRectangleTexture.getRegionHeight());
        informationButton.addListener(new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sound.pauseGameBGM();
                Sound.loopOpeningBGMGradually();
                if (ModeTracker.mode == ModeTracker.Mode.SINGLE_PLAYER) {
                    ScreenTracker.transitionScreen.transition(Direction.LEFT, ScreenTracker.singlePlayerSettingsScreen);
                } else {
                    ScreenTracker.transitionScreen.transition(Direction.LEFT, ScreenTracker.twoPlayerSettingsScreen);
                }
            }
        });
        stage.addActor(informationButton);
    }
    private void buildHelpButton(TextureRegion optionsRectangleTexture, float offsetFromOriginalWidth) {
        optionsButton = new Image(optionsRectangleTexture);
        optionsButton.setBounds(543 + offsetFromOriginalWidth, Launch.SCREEN_HEIGHT - 1136, optionsRectangleTexture.getRegionWidth(), optionsRectangleTexture.getRegionHeight());
        optionsButton.addListener(new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sound.pauseGameBGM();
                Sound.loopOpeningBGMGradually();
                ScreenTracker.transitionScreen.transition(Direction.RIGHT, ScreenTracker.optionsScreen);
            }
        });
        stage.addActor(optionsButton);
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
            updateCurrentPlayer(currentPlayer);
            updateChosenNumber(state);
            updateMessageArea(state);
            updateBoardMap(state);
            updateNumberScroller(state);
        } else {
            updateBoardMap(state);
            if (Persistence.getInstance().isInPlay()) {
                endingSequenceDelegate.animateEndingSequence(state, currentPlayer);
                Persistence.getInstance().setInPlay(false);
            } else {
                endingSequenceDelegate.recreateEndingInstantly(state, currentPlayer);
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
    private void updateBoardMap(CortexState state) {
        Map<Integer, Integer> boardMap = state.getCoordinateNumberMap();
        for (Map.Entry<Integer, Integer> entry : boardMap.entrySet()) {
            int coordinate = entry.getKey();
            int number = entry.getValue();
            if (number != -1) {
                board.updateCell(coordinate, number);
            }
        }
    }
    private void updateNumberScroller(CortexState state) {
        ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
        numberScroller.update(availableNumbers);
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
        buildPlayScreenStage(width, height);
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
        boolean dialogAlreadyExists = checkIfDialogAlreadyExists();
        final Persistence persistence = Persistence.getInstance();
        if (!persistence.isInPlay()) {
            ScreenTracker.transitionScreen.transition(Direction.LEFT, ScreenTracker.titleScreen);
        } else if (!dialogAlreadyExists) {
            Dialog dialog = buildQuitCancelDialog();
            dialog.show(stage);
        }
    }
    private Dialog buildQuitCancelDialog() {
        Dialog dialog = CortexDialog.createQuitCancelDialog(new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Persistence persistence = Persistence.getInstance();
                persistence.setInPlay(false);
                Sound.stopGameBGM();
                Sound.loopOpeningBGMGradually();
                ScreenTracker.transitionScreen.transition(Direction.LEFT, ScreenTracker.titleScreen);
            }
        });
        return dialog;
    }
    private boolean checkIfDialogAlreadyExists() {
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
