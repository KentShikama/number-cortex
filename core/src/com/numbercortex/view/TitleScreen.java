package com.numbercortex.view;

import iap.CrossPlatformIAP;
import iap.IAPListener;
import libgdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.numbercortex.ModeTracker;
import com.numbercortex.Persistence;
import com.numbercortex.logic.LogicConstructor;
import com.numbercortex.view.TransitionScreen.Direction;

class TitleScreen extends HomeScreen {

    static final String TAG = "Title Screen";

    private static final String PLAY_BUTTON = "Play";
    private static final String PASS_AND_PLAY_BUTTON = "Pass & Play";
    private static final String OPTIONS = "Options";
    private static final String MORE = "More";

    private CrossPlatformIAP IAP;
    
    private boolean reloadScreen;

    TitleScreen(Game game, CrossPlatformIAP IAP) {
        super(game);
        this.IAP = IAP;
    }
    
    class TwoPlayerButtonListener extends ClickListenerWithSound {
        private boolean isDisabled;
        TwoPlayerButtonListener(boolean isDisabled) {
            this.isDisabled = isDisabled;
        }
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if (isDisabled) {
                handleTwoPlayerIAP();
            } else {
                transitionToTwoPlayerSettingsScreen();   
            }
        }
        private void handleTwoPlayerIAP() {
            if (IAP == null) {
                Dialog errorDialog = CortexDialog.createConfirmationDialog("This feature is not supported on this device.");
                errorDialog.show(stage);
            } else {
                ClickListenerWithSound purchaseListener = buildPurchaseListener();
                ClickListenerWithSound redeemListener = buildRedeemListener();
                Dialog purchaseDialog = CortexDialog.createPurchaseRedeemCancelDialog(purchaseListener, redeemListener);
                purchaseDialog.show(stage);
            }
        }
        private ClickListenerWithSound buildPurchaseListener() {
            ClickListenerWithSound purchaseListener = new ClickListenerWithSound() {
                public void clicked(InputEvent event, float x, float y) {
                    IAPListener listener = new IAPListener() {
                        @Override
                        public void success() {
                            LogicConstructor.writeCorrectCode();
                            reloadScreen = true;
                        }
                        @Override
                        public void failure(String errorMessage) {
                            Dialog errorDialog = CortexDialog.createConfirmationDialog(errorMessage);
                            errorDialog.show(stage);
                        }                        
                    };
                    IAP.setPurchaseListener(listener);
                    IAP.purchase();
                }
            };
            return purchaseListener;
        }
        private ClickListenerWithSound buildRedeemListener() {
            ClickListenerWithSound redeemListener = new ClickListenerWithSound() {
                public void clicked(InputEvent event, float x, float y) {
                    IAPListener listener = new IAPListener() {
                        @Override
                        public void success() {
                            LogicConstructor.writeCorrectCode();
                            reloadScreen = true;
                        }
                        @Override
                        public void failure(String errorMessage) {
                            Dialog errorDialog = CortexDialog.createConfirmationDialog(errorMessage);
                            errorDialog.show(stage);
                        }                        
                    };
                    IAP.setPurchaseListener(listener);
                    IAP.restore();
                }
            };
            return redeemListener;
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
        boolean isDisabled = getWhetherTwoPlayerIsDisabled();
        // Pass in isDisabled to require two player IAP
        HomeScreenButton passAndPlayButton = new HomeScreenButton(PASS_AND_PLAY_BUTTON, 1, new TwoPlayerButtonListener(false), false);
        HomeScreenButton optionsButton = new HomeScreenButton(OPTIONS, 2, ScreenTracker.optionsScreen, null);
        HomeScreenButton moreButton = new HomeScreenButton(MORE, 3, ScreenTracker.moreScreen, null);
        stage.addActor(playButton);
        stage.addActor(passAndPlayButton);
        stage.addActor(optionsButton);
        stage.addActor(moreButton);
    }
    private boolean getWhetherTwoPlayerIsDisabled() {
        Persistence persistence = Persistence.getInstance();
        boolean isDisabled = ! LogicConstructor.isCodeCorrect(persistence.getTwoPlayerCode());
        return isDisabled;
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
