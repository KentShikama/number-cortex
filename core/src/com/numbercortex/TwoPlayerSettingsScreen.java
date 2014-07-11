package com.numbercortex;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class TwoPlayerSettingsScreen implements Screen {

	private static final String TAG = TwoPlayerSettingsScreen.class.getCanonicalName();

	private Stage stage;
	private Game game;

	private static final int CHECKBOX_LENGTH = 84;

	private static final String SETTINGS_BACKGROUND = "settings_background";
	private static final String SETTINGS_TITLE = "settings_title";
	private static final String MENU_BODY = "menu_body";

	public TwoPlayerSettingsScreen(Game game) {
		this.game = game;
		stage = ((Launch) game).getStage();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		stage.clear();

		CortexPreferences preferences = CortexPreferences.getInstance();
		GameSettings twoPlayerGameSettings = preferences.getTwoPlayerGameSettings();

		BackgroundScreen background = new BackgroundScreen(Launch.SEA_BLUE, Assets.backgroundTexture);
		stage.addActor(background);

		Drawable emptyCheckbox = Assets.settingsSkin.getDrawable("empty_checkbox");
		Drawable checkedCheckbox = Assets.settingsSkin.getDrawable("checked_checkbox");

		buildDiagonalsCheckbox(twoPlayerGameSettings, emptyCheckbox, checkedCheckbox);
		buildFourSquareCheckbox(twoPlayerGameSettings, emptyCheckbox, checkedCheckbox);

		buildMusicCheckbox(preferences, emptyCheckbox, checkedCheckbox);

	}

	private void buildDiagonalsCheckbox(final GameSettings gameSettings, Drawable emptyCheckbox,
			Drawable checkedCheckbox) {
		final ImageButton diagonalsCheckbox = new ImageButton(emptyCheckbox, emptyCheckbox, checkedCheckbox);
		diagonalsCheckbox.setBounds(416, Launch.SCREEN_HEIGHT - 488, CHECKBOX_LENGTH, CHECKBOX_LENGTH);
		diagonalsCheckbox.setChecked(gameSettings.isDiagonals());
		diagonalsCheckbox.left();
		diagonalsCheckbox.bottom();
		diagonalsCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameSettings.setDiagonals(diagonalsCheckbox.isChecked());
			}
		});
		stage.addActor(diagonalsCheckbox);
	}

	private void buildFourSquareCheckbox(final GameSettings gameSettings, Drawable emptyCheckbox,
			Drawable checkedCheckbox) {
		final ImageButton fourSquareCheckbox = new ImageButton(emptyCheckbox, emptyCheckbox, checkedCheckbox);
		fourSquareCheckbox.setBounds(416, Launch.SCREEN_HEIGHT - 593, CHECKBOX_LENGTH, CHECKBOX_LENGTH);
		fourSquareCheckbox.setChecked(gameSettings.isFourSquare());
		fourSquareCheckbox.left();
		fourSquareCheckbox.bottom();
		fourSquareCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameSettings.setFourSquare(fourSquareCheckbox.isChecked());
			}
		});
		stage.addActor(fourSquareCheckbox);
	}

	private void buildMusicCheckbox(final CortexPreferences preferences, Drawable emptyCheckbox,
			Drawable checkedCheckbox) {
		final ImageButton musicCheckbox = new ImageButton(emptyCheckbox, emptyCheckbox, checkedCheckbox);
		musicCheckbox.setBounds(416, Launch.SCREEN_HEIGHT - 844, CHECKBOX_LENGTH, CHECKBOX_LENGTH);
		musicCheckbox.setChecked(preferences.isMusicEnabled());
		musicCheckbox.left();
		musicCheckbox.bottom();
		musicCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				preferences.setMusicEnabled(musicCheckbox.isChecked());
			}
		});
		stage.addActor(musicCheckbox);
	}

	@Override
	public void hide() {
		CortexPreferences.getInstance().save();
	}

	@Override
	public void resume() {
		Assets.loadSettings();
	}

	@Override
	public void dispose() {}

	@Override
	public void pause() {}

}
