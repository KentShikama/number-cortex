package com.numbercortex.view;

import libgdx.Game;
import chartboost.ChartBoostListener;
import chartboost.CrossPlatformChartboost;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.numbercortex.Persistence;
import com.numbercortex.view.TransitionScreen.Direction;

class MoreScreen extends HomeScreen {

    static final String TAG = "More Screen";

    private static final String MORE_GAMES_BUTTON = "More Games";
    private static final String RATE_GAME_BUTTON = "Rate Game";
    private static final String WEBSITE_BUTTON = "Website";
    private static final String CREDITS = "Credits";

    private CrossPlatformChartboost chartboost;

    MoreScreen(Game game, CrossPlatformChartboost chartboost) {
        super(game);
        this.chartboost = chartboost;
    }

    @Override
    void setUpBackKeyCatch() {
        Gdx.input.setCatchBackKey(true);
        backKey = false;
    }
    @Override
    void buildButtons(final Stage stage) {
        ClickListener moreGamesButtonListener = new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ChartBoostListener listener = new ChartBoostListener() {
                    @Override
                    public void showMoreApps() {}
                    @Override
                    public void didDismissMoreApps() {}
                    @Override
                    public void didFailToLoadMoreApps(String errorMessage) {
                        showErrorMessage(errorMessage);
                    }    
                };
                chartboost.setListener(listener);
                chartboost.showMoreApps();
            }
        };
        HomeScreenButton moreGamesButton = new HomeScreenButton(MORE_GAMES_BUTTON, 0, moreGamesButtonListener);
        HomeScreenButton rateGameButton = new HomeScreenButton(RATE_GAME_BUTTON, 1, null);
        ClickListener websiteButtonListener = new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("http://www.numbercortex.com");
            }
        };
        HomeScreenButton websiteButton = new HomeScreenButton(WEBSITE_BUTTON, 2, websiteButtonListener);
        HomeScreenButton creditsButton = new HomeScreenButton(CREDITS, 3, ScreenTracker.creditsScreen, null);
        stage.addActor(moreGamesButton);
        stage.addActor(rateGameButton);
        stage.addActor(websiteButton);
        stage.addActor(creditsButton);
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
            Dialog errorDialog = CortexDialog.createConfirmationDialog(errorMessage, null);
            errorDialog.show(stage);
        }
    }
    
    @Override
    public void pause() {
        Persistence persistence = Persistence.getInstance();
        persistence.setCurrentScreen(TAG);
    }

}
