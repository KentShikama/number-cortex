package com.numbercortex.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.numbercortex.Persistence;
import com.numbercortex.view.TransitionScreen.Direction;

import libgdx.Game;

class MoreScreen extends HomeScreen {

    static final String TAG = "More Screen";

    private static final String MORE_GAMES_BUTTON = "More Games";
    private static final String RATE_GAME_BUTTON = "Rate Game";
    private static final String HELP_BUTTON = "Help";
    private static final String CREDITS = "Credits";

    private String appLink;

    MoreScreen(Game game, String appLink) {
        super(game);
        this.appLink = appLink;
    }

    @Override
    void setUpBackKeyCatch() {
        Gdx.input.setCatchBackKey(true);
        backKey = false;
    }
    @Override
    void buildButtons(final Stage stage) {
        HomeScreenButton moreGamesButton = buildMoreGamesButton();
        HomeScreenButton rateGameButton = buildRateGameButton();
        HomeScreenButton websiteButton = buildWebsiteButton();
        HomeScreenButton creditsButton = new HomeScreenButton(CREDITS, 3, ScreenTracker.creditsScreen, null);
        stage.addActor(moreGamesButton);
        stage.addActor(rateGameButton);
        stage.addActor(websiteButton);
        stage.addActor(creditsButton);
    }
    private HomeScreenButton buildMoreGamesButton() {
        ClickListener moreGamesButtonListener = new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("http://www.numbercortex.com/");
            }
        };
        HomeScreenButton moreGamesButton = new HomeScreenButton(MORE_GAMES_BUTTON, 0, moreGamesButtonListener);
        return moreGamesButton;
    }
    private HomeScreenButton buildRateGameButton() {
        ClickListener rateGameButtonListener;
        if (appLink == null) {
            rateGameButtonListener = null;
        } else {
            rateGameButtonListener = new ClickListenerWithSound() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.net.openURI(appLink);
                }
            };
        }
        HomeScreenButton rateGameButton = new HomeScreenButton(RATE_GAME_BUTTON, 1, rateGameButtonListener);
        return rateGameButton;
    }
    private HomeScreenButton buildWebsiteButton() {
        ClickListener websiteButtonListener = new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("http://www.numbercortex.com/faq/");
            }
        };
        HomeScreenButton websiteButton = new HomeScreenButton(HELP_BUTTON, 2, websiteButtonListener);
        return websiteButton;
    }
    @Override
    void buildBottomNavigation(Stage stage) {
        BackBottomNavigation backBottomNavigation = new BackBottomNavigation("Home", ScreenTracker.titleScreen);
        stage.addActor(backBottomNavigation);
    }
    @Override
    void handleBackKey() {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            backKey = true;
        } else if (backKey) {
            backKey = false;
            ScreenTracker.transitionScreen.transition(Direction.LEFT, ScreenTracker.titleScreen);
        }
    }

    void showErrorMessage(String errorMessage) {
        if (game.getScreen() instanceof MoreScreen) {
            Dialog errorDialog = CortexDialog.createConfirmationDialog(errorMessage);
            errorDialog.show(stage);
        }
    }

    @Override
    public void pause() {
        Persistence persistence = Persistence.getInstance();
        persistence.setCurrentScreen(TAG);
    }

}
