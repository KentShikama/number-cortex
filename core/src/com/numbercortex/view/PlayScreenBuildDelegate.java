package com.numbercortex.view;

import libgdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.numbercortex.GameSettings;
import com.numbercortex.ModeTracker;
import com.numbercortex.Persistence;
import com.numbercortex.view.TransitionScreen.Direction;

class PlayScreenBuildDelegate {

    private Game game;
    private Stage stage;
    private PlayScreenControls controls;

    private GameSettings settings;
    private Persistence preferences;

    private DragAndDropHandler handler = DragAndDropHandler.getInstance();

    PlayScreenBuildDelegate(Game game, Stage stage, PlayScreenControls controls) {
        this.game = game;
        this.stage = stage;
        this.controls = controls;
    }

    void setGameSettingsAndPreferences(GameSettings settings, Persistence preferences) {
        this.settings = settings;
        this.preferences = preferences;
    }

    void buildPlayScreenStage(int width, int height) {
        stage.getViewport().update(width, height, true);
        stage.clear();
        
        float worldWidth = stage.getViewport().getWorldWidth();
        Color backgroundColor = Persistence.getInstance().isBlue() ? Launch.SEA_BLUE : Launch.RETRO_RED;
        Background plainBackground = new Background(backgroundColor, worldWidth);
        stage.addActor(plainBackground);
        
        buildMessageArea();
        buildBoard(settings, preferences);
        buildNumberScroller();
        buildBottomButtons();
        controls.getBoard().clearBoard();
    }
    private void buildMessageArea() {
        MessageArea messageArea = MessageArea.createMessageArea(stage);
        handler.notifyMessageAreaConstrucion(messageArea);
        controls.setMessageArea(messageArea);
    }
    private void buildBoard(GameSettings settings, Persistence preferences) {
        boolean isBlue = preferences.isBlue();
        int numberOfRows = settings.getNumberOfRows();
        NumberCortexBoard board = NumberCortexBoard.createNumberCortexBoard(stage, isBlue, numberOfRows);
        handler.notifyBoardConstruction(board);
        controls.setBoard(board);
    }
    private void buildNumberScroller() {
        NumberScroller numberScroller = NumberScroller.createNumberScroller(stage);
        controls.setNumberScroller(numberScroller);
    }
    private void buildBottomButtons() {
        TextureRegion exitRectangleTexture = Assets.gameSkin.getRegion("exit");
        TextureRegion informationRectangleTexture = Assets.gameSkin.getRegion("information");
        TextureRegion optionsRectangleTexture = Assets.gameSkin.getRegion("options");
        float worldWidth = stage.getViewport().getWorldWidth();
        float offsetFromOriginalWidth = (worldWidth - Launch.SCREEN_WIDTH) / 2;
        bulidExitButton(exitRectangleTexture, offsetFromOriginalWidth);
        buildInformationButton(informationRectangleTexture, offsetFromOriginalWidth);
        buildOptionsButton(optionsRectangleTexture, offsetFromOriginalWidth);
    }
    private void bulidExitButton(TextureRegion exitRectangleTexture, float offsetFromOriginalWidth) {
        Image exitButton = new Image(exitRectangleTexture);
        exitButton.setBounds(44 + offsetFromOriginalWidth, Launch.SCREEN_HEIGHT - 1136, exitRectangleTexture.getRegionWidth(), exitRectangleTexture.getRegionHeight());
        exitButton.addListener(new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Dialog dialog = CortexDialog.createGameQuitCancelDialog();
                dialog.show(stage);
            }
        });
        stage.addActor(exitButton);
        controls.setExitButton(exitButton);
    }
    private void buildInformationButton(TextureRegion informationRectangleTexture, float offsetFromOriginalWidth) {
        Image informationButton = new Image(informationRectangleTexture);
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
        controls.setInformationButton(informationButton);
    }
    private void buildOptionsButton(TextureRegion optionsRectangleTexture, float offsetFromOriginalWidth) {
        Image optionsButton = new Image(optionsRectangleTexture);
        optionsButton.setBounds(543 + offsetFromOriginalWidth, Launch.SCREEN_HEIGHT - 1136, optionsRectangleTexture.getRegionWidth(), optionsRectangleTexture.getRegionHeight());
        optionsButton.addListener(new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sound.pauseGameBGM();
                Sound.loopOpeningBGMGradually();
                ScreenTracker.transitionScreen.transition(Direction.RIGHT, ScreenTracker.optionsScreen);
            }
        });
        optionsButton.addListener(new ActorGestureListener() {
            public boolean longPress(Actor actor, float x, float y) {
                System.out.println("Switching background color...");
                Persistence persistence = Persistence.getInstance();
                persistence.setBlue(!persistence.isBlue());
                game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                return true;          
            }
        });
        stage.addActor(optionsButton);
        controls.setOptionsButton(optionsButton);
    }
}
