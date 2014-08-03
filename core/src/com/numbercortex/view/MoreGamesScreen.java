package com.numbercortex.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.numbercortex.Persistence;
import com.numbercortex.view.TransitionScreen.Direction;
import libgdx.Game;

public class MoreGamesScreen extends GameScreen {

    public static final String TAG = "More Games Screen";
    
    private Image gear;
    private static final String GEAR = "options_icon";
    
    MoreGamesScreen(Game game) {
        super(game);
    }
    
    @Override
    public void show() {
        FitViewport fitViewport = new FitViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
        stage = new Stage(fitViewport);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
        backKey = false;
        addGear(stage);
    }
    private void addGear(Stage stage) {
        TextureRegion gearTexture = Assets.settingsSkin.getRegion(GEAR);
        gear = new Image(gearTexture);
        gear.setPosition(320 - 51, Launch.SCREEN_HEIGHT - 568 - 51);
        gear.setOrigin(gear.getWidth() / 2, gear.getHeight() / 2);
        stage.addActor(gear);
    }
    
    void removeGear() {
        AlphaAction fadeOutAction = Actions.fadeOut(0.4f);
        gear.addAction(fadeOutAction);        
    }
    void rotateGear() {
        RotateByAction rotateGearAction = Actions.rotateBy(4000f, 30);
        gear.addAction(rotateGearAction);
    }

    @Override
    public void pause() {
        Persistence persistence = Persistence.getInstance();
        persistence.setCurrentScreen(MoreScreen.TAG);
    }

    @Override
    public void render(float delta) {
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        handleBackKey();
        stage.act(delta);
        stage.draw();
    }
    private void handleBackKey() {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            backKey = true;
        } else if (backKey) {
            backKey = false;
            ScreenTracker.transitionScreen.transition(Direction.LEFT, ScreenTracker.moreScreen);
        }
    }
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    void showErrorMessage(String errorMessage) {
        Dialog errorDialog = CortexDialog.createConfirmationDialog(errorMessage, new ClickListenerWithSound() {
            public void clicked(InputEvent event, float x, float y) {
                backKey = true;
            }
        });
        errorDialog.show(stage);
    }
    
    void setBackKeyToTrue() {
        backKey = true;
    }

}
