package com.numbercortex.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.numbercortex.ModeTracker;
import com.numbercortex.Persistence;
import com.numbercortex.view.TransitionScreen.Direction;

import libgdx.Game;

class TitleScreen extends HomeScreen {

    static final String TAG = "Title Screen";

    private static final String PLAY_BUTTON = "Play";
    private static final String PASS_AND_PLAY_BUTTON = "Pass & Play";
    private static final String OPTIONS = "Options";
    private static final String MORE = "More";

    private boolean reloadScreen;

    TitleScreen(Game game) {
        super(game);
    }
    
    class TwoPlayerButtonListener extends ClickListenerWithSound {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            transitionToTwoPlayerSettingsScreen();
        }

        private void transitionToTwoPlayerSettingsScreen() {
            ModeTracker.mode = ModeTracker.Mode.TWO_PLAYER;
            ScreenTracker.transitionScreen.transition(Direction.RIGHT, ScreenTracker.twoPlayerSettingsScreen);
        }
    }

    @Override
    void setUpBackKeyCatch() {
        Gdx.input.setCatchBackKey(false);
    }
    @Override
    void buildButtons(Stage stage) {
        HomeScreenButton playButton = new HomeScreenButton(PLAY_BUTTON, 0, ScreenTracker.levelsScreen, ModeTracker.Mode.SINGLE_PLAYER);
        HomeScreenButton passAndPlayButton = new HomeScreenButton(PASS_AND_PLAY_BUTTON, 1, new TwoPlayerButtonListener(), false);
        HomeScreenButton optionsButton = new HomeScreenButton(OPTIONS, 2, ScreenTracker.optionsScreen, null);
        HomeScreenButton moreButton = new HomeScreenButton(MORE, 3, ScreenTracker.moreScreen, null);
        stage.addActor(playButton);
        stage.addActor(passAndPlayButton);
        stage.addActor(optionsButton);
        stage.addActor(moreButton);
    }

    @Override
    void buildBottomNavigation(Stage stage) {}
    
    @Override
    public void render(float delta) {
        super.render(delta);
        handleReloadScreen();
    }
    @Override
    void handleBackKey() {}
    private void handleReloadScreen() {
        if (reloadScreen) {
            reloadScreen = false;
            ScreenTracker.titleScreen.show();
        }
    }
    
    @Override
    public void pause() {
        Persistence persistence = Persistence.getInstance();
        persistence.setCurrentScreen(TAG);
    }

}
