package com.numberquarto;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SettingsScreen implements Screen {

	private static final String TAG = SettingsScreen.class.getCanonicalName();

	private Stage stage;
	private Skin skin = Assets.settingsSkin;

	private ImageButton diagonalsCheckbox;
	private ImageButton fourSquareCheckbox;
	private ImageButton musicCheckbox;
	private ImageButton easyButton;
	private ImageButton mediumButton;
	private ImageButton hardButton;
	private RatingGroup difficultyGroup;

	private ImageButton playButton;

	private ImageButton resumeButton;

	private ImageButton exitButton;

	private Game game;

	private static final int CHECKBOX_LENGTH = 84;
	private static final int STAR_WIDTH = 78;
	private static final int STAR_HEIGHT = 75;

	private static final TextureRegion PLAY_BUTTON_TEXTURE = Assets.settingsSkin
			.getRegion("play_button");
	private static final int RIGHT_BUTTON_WIDTH = PLAY_BUTTON_TEXTURE
			.getRegionWidth();
	private static final int RIGHT_BUTTON_HEIGHT = PLAY_BUTTON_TEXTURE
			.getRegionHeight();

	private static final TextureRegion EXIT_BUTTON_TEXTURE = Assets.settingsSkin
			.getRegion("exit_button");
	private static final int LEFT_BUTTON_WIDTH = EXIT_BUTTON_TEXTURE
			.getRegionWidth();
	private static final int LEFT_BUTTON_HEIGHT = EXIT_BUTTON_TEXTURE
			.getRegionHeight();

	private static final String SETTINGS_BACKGROUND = "settings_background";
	private static final String SETTINGS_TITLE = "settings_title";
	private static final String MENU_BODY = "menu_body";

	class RatingGroup {
		private ArrayList<Button> list = new ArrayList<Button>();
		private boolean isDisabled;
		private int rating;

		public boolean isDisabled() {
			return isDisabled;
		}

		public void setDisabled(boolean isDisabled) {
			this.isDisabled = isDisabled;
			for (Button button : list) {
				button.setDisabled(isDisabled);
			}
		}

		public void add(Button button) {
			list.add(button);
		}

		public void toggleRating() {
			if (list.size() < rating) {
				Gdx.app.log(TAG, "Some buttons are missing...");
			}
			if (rating == list.size()) {
				rating = 1;
			} else {
				rating++;
			}
			for (int i = 0; i < rating; i++) {
				list.get(i).setChecked(true);
			}
			for (int j = rating; j < list.size(); j++) {
				list.get(j).setChecked(false);
			}
		}

		public void addToContentsToStage(Stage stage) {
			stage.addActor(easyButton);
			stage.addActor(mediumButton);
			stage.addActor(hardButton);
		}
	}

	class DifficultyGroupListener extends ClickListener {
		public void clicked(InputEvent event, float x, float y) {
			if (difficultyGroup.isDisabled())
				return;
			difficultyGroup.toggleRating();
		}
	}

	class SettingsTitle extends Actor {
		private TextureRegion settingsTitle;

		SettingsTitle() {
			settingsTitle = skin.getRegion(SETTINGS_TITLE);
			this.setBounds(0, Launch.SCREEN_HEIGHT - 245,
					settingsTitle.getRegionWidth(),
					settingsTitle.getRegionHeight());
		}

		@Override
		public void draw(Batch batch, float alpha) {
			batch.draw(settingsTitle, this.getX(), this.getY());
		}
	}

	class MenuBody extends Actor {
		private TextureRegion menuBody;

		MenuBody() {
			menuBody = skin.getRegion(MENU_BODY);
			this.setBounds(0, Launch.SCREEN_HEIGHT - 875,
					menuBody.getRegionWidth(), menuBody.getRegionHeight());
		}

		@Override
		public void draw(Batch batch, float alpha) {
			batch.draw(menuBody, this.getX(), this.getY());
		}
	}

	public SettingsScreen(Game game) {
		this.game = game;
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
		stage = new Stage(new FitViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT));
		Gdx.input.setInputProcessor(stage);

		ScreenBackground background = new ScreenBackground(skin, SETTINGS_BACKGROUND);
		stage.addActor(background);

		SettingsTitle title = new SettingsTitle();
		stage.addActor(title);

		MenuBody menuBody = new MenuBody();
		stage.addActor(menuBody);

		Drawable emptyCheckbox = skin.getDrawable("empty_checkbox");
		Drawable checkedCheckbox = skin.getDrawable("checked_checkbox");

		buildDiagonalsCheckbox(emptyCheckbox, checkedCheckbox);
		buildFourSquareCheckbox(emptyCheckbox, checkedCheckbox);

		Drawable emptyStar = skin.getDrawable("empty_star");
		Drawable fullStar = skin.getDrawable("full_star");

		buildEasyButton(emptyStar, fullStar);
		buildMediumButton(emptyStar, fullStar);
		buildHardButton(emptyStar, fullStar);
		
		buildDifficultyGroup();

		buildMusicCheckbox(emptyCheckbox, checkedCheckbox);

		buildPlayButton();
		buildResumeButton();
		buildExitButton();

		addButtonsToStage();
	}

	private void buildDiagonalsCheckbox(Drawable emptyCheckbox,
			Drawable checkedCheckbox) {
		diagonalsCheckbox = new ImageButton(emptyCheckbox, emptyCheckbox,
				checkedCheckbox);
		diagonalsCheckbox.setBounds(416, Launch.SCREEN_HEIGHT - 488,
				CHECKBOX_LENGTH, CHECKBOX_LENGTH);
		diagonalsCheckbox.setChecked(true);
		diagonalsCheckbox.left();
		diagonalsCheckbox.bottom();
	}

	private void buildDifficultyGroup() {
		difficultyGroup = new RatingGroup();
		difficultyGroup.add(easyButton);
		difficultyGroup.add(mediumButton);
		difficultyGroup.add(hardButton);
		difficultyGroup.toggleRating();
		difficultyGroup.toggleRating();
	}

	private void buildFourSquareCheckbox(Drawable emptyCheckbox,
			Drawable checkedCheckbox) {
		fourSquareCheckbox = new ImageButton(emptyCheckbox, emptyCheckbox,
				checkedCheckbox);
		fourSquareCheckbox.setBounds(416, Launch.SCREEN_HEIGHT - 593,
				CHECKBOX_LENGTH, CHECKBOX_LENGTH);
		fourSquareCheckbox.left();
		fourSquareCheckbox.bottom();
	}

	private void buildEasyButton(Drawable emptyStar, Drawable fullStar) {
		easyButton = new ImageButton(emptyStar, emptyStar, fullStar);
		easyButton.setBounds(351, Launch.SCREEN_HEIGHT - 721, STAR_WIDTH,
				STAR_HEIGHT);
		easyButton.removeListener(easyButton.getClickListener());
		easyButton.addListener(new DifficultyGroupListener());
	}

	private void buildMediumButton(Drawable emptyStar, Drawable fullStar) {
		mediumButton = new ImageButton(emptyStar, emptyStar, fullStar);
		mediumButton.setBounds(351 + 91, Launch.SCREEN_HEIGHT - 721,
				STAR_WIDTH, STAR_HEIGHT);
		mediumButton.removeListener(mediumButton.getClickListener());
		mediumButton.addListener(new DifficultyGroupListener());
	}

	private void buildHardButton(Drawable emptyStar, Drawable fullStar) {
		hardButton = new ImageButton(emptyStar, emptyStar, fullStar);
		hardButton.setBounds(351 + (91 * 2), Launch.SCREEN_HEIGHT - 721,
				STAR_WIDTH, STAR_HEIGHT);
		hardButton.removeListener(hardButton.getClickListener());
		hardButton.addListener(new DifficultyGroupListener());
	}

	private void buildMusicCheckbox(Drawable emptyCheckbox,
			Drawable checkedCheckbox) {
		musicCheckbox = new ImageButton(emptyCheckbox, emptyCheckbox,
				checkedCheckbox);
		musicCheckbox.setBounds(416, Launch.SCREEN_HEIGHT - 844,
				CHECKBOX_LENGTH, CHECKBOX_LENGTH);
		musicCheckbox.setChecked(true);
		musicCheckbox.left();
		musicCheckbox.bottom();
	}

	private void buildPlayButton() {
		Drawable playButtonSkin = skin.getDrawable("play_button");
		playButton = new ImageButton(playButtonSkin);
		playButton.setBounds(296, Launch.SCREEN_HEIGHT - 1085,
				RIGHT_BUTTON_WIDTH, RIGHT_BUTTON_HEIGHT);
		playButton.addListener(new ClickListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new PlayScreen(game));
				return true;
			}
		});
	}

	private void buildResumeButton() {
		Drawable resumeButtonSkin = skin.getDrawable("resume_button");
		resumeButton = new ImageButton(resumeButtonSkin);
		resumeButton.setBounds(296, Launch.SCREEN_HEIGHT - 1085,
				RIGHT_BUTTON_WIDTH, RIGHT_BUTTON_HEIGHT);
	}

	private void buildExitButton() {
		Drawable exitButtonSkin = skin.getDrawable("exit_button");
		exitButton = new ImageButton(exitButtonSkin);
		exitButton.setBounds(40, Launch.SCREEN_HEIGHT - 1085,
				LEFT_BUTTON_WIDTH, LEFT_BUTTON_HEIGHT);
		exitButton.addListener(new ClickListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				game.setScreen(new TitleScreen(game));
				return true;
			}
		});
	}

	private void addButtonsToStage() {
		stage.addActor(diagonalsCheckbox);
		stage.addActor(fourSquareCheckbox);
		difficultyGroup.addToContentsToStage(stage);
		stage.addActor(musicCheckbox);
		stage.addActor(playButton);
		stage.addActor(exitButton);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
