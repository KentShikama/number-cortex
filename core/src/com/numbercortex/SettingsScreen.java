package com.numbercortex;

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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SettingsScreen implements Screen {

	class DifficultyGroupListener extends ClickListener {
		private RatingGroup group;
		private CortexPreferences preferences;
		DifficultyGroupListener(RatingGroup group, CortexPreferences preferences) {
			this.group = group;
			this.preferences = preferences;
		}
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if (group.isDisabled())
				return;
			group.toggleRating();
			preferences.setDifficulty(group.getRating());
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
	class RatingGroup {
		private ArrayList<Button> list = new ArrayList<Button>();
		private boolean isDisabled;
		private int rating;

		public void add(Button button) {
			list.add(button);
		}

		public void addToContentsToStage(Stage stage) {
			for (Button button : list) {
				stage.addActor(button);
			}
		}

		public int getRating() {
			return rating;
		}

		public boolean isDisabled() {
			return isDisabled;
		}

		public void setDisabled(boolean isDisabled) {
			this.isDisabled = isDisabled;
			for (Button button : list) {
				button.setDisabled(isDisabled);
			}
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

	private static final String TAG = SettingsScreen.class.getCanonicalName();

	private Stage stage;
	private Skin skin = Assets.settingsSkin;
	private RatingGroup difficultyGroup;

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
	private static final TextureRegion QUIT_BUTTON_TEXTURE = Assets.settingsSkin
			.getRegion("quit_button");
	private static final int LEFT_BUTTON_WIDTH = QUIT_BUTTON_TEXTURE
			.getRegionWidth();

	private static final int LEFT_BUTTON_HEIGHT = QUIT_BUTTON_TEXTURE
			.getRegionHeight();

	private static final String SETTINGS_BACKGROUND = "settings_background";

	private static final String SETTINGS_TITLE = "settings_title";

	private static final String MENU_BODY = "menu_body";

	public SettingsScreen(Game game) {
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

		ScreenBackground background = new ScreenBackground(skin, SETTINGS_BACKGROUND);
		stage.addActor(background);

		SettingsTitle title = new SettingsTitle();
		stage.addActor(title);

		MenuBody menuBody = new MenuBody();
		stage.addActor(menuBody);

		Drawable emptyCheckbox = skin.getDrawable("empty_checkbox");
		Drawable checkedCheckbox = skin.getDrawable("checked_checkbox");

		buildDiagonalsCheckbox(preferences, emptyCheckbox, checkedCheckbox);
		buildFourSquareCheckbox(preferences, emptyCheckbox, checkedCheckbox);

		Drawable emptyStar = skin.getDrawable("empty_star");
		Drawable fullStar = skin.getDrawable("full_star");

		ImageButton easyButton = buildEasyButton(preferences, emptyStar, fullStar);
		ImageButton mediumButton = buildMediumButton(preferences, emptyStar, fullStar);
		ImageButton hardButton = buildHardButton(preferences, emptyStar, fullStar);
		
		buildDifficultyGroup(preferences, easyButton, mediumButton, hardButton);

		buildMusicCheckbox(preferences, emptyCheckbox, checkedCheckbox);

		buildPlayButton();
		buildResumeButton();
		buildQuitButton();
	}

	private void buildDiagonalsCheckbox(final CortexPreferences preferences, Drawable emptyCheckbox,
			Drawable checkedCheckbox) {
		final ImageButton diagonalsCheckbox = new ImageButton(emptyCheckbox, emptyCheckbox,
				checkedCheckbox);
		diagonalsCheckbox.setBounds(416, Launch.SCREEN_HEIGHT - 488,
				CHECKBOX_LENGTH, CHECKBOX_LENGTH);
		diagonalsCheckbox.setChecked(preferences.isDiagonalsEnabled());
		diagonalsCheckbox.left();
		diagonalsCheckbox.bottom();
		diagonalsCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				preferences.setDiagonalsEnabled(diagonalsCheckbox.isChecked());
			}
		});
		stage.addActor(diagonalsCheckbox);
	}
	
	private void buildDifficultyGroup(CortexPreferences preferences, ImageButton...buttons) {
		difficultyGroup = new RatingGroup();
		DifficultyGroupListener listener = new DifficultyGroupListener(difficultyGroup, preferences);
		for (int i = 0; i < buttons.length; i++) {
			difficultyGroup.add(buttons[i]);
			buttons[i].addListener(listener);
		}
		for (int i = 0; i < preferences.getDifficulty(); i++) {
			difficultyGroup.toggleRating();
		}
		difficultyGroup.addToContentsToStage(stage);
	}

	private ImageButton buildEasyButton(CortexPreferences preferences, Drawable emptyStar, Drawable fullStar) {
		ImageButton easyButton = new ImageButton(emptyStar, emptyStar, fullStar);
		easyButton.setBounds(351, Launch.SCREEN_HEIGHT - 721, STAR_WIDTH,
				STAR_HEIGHT);
		easyButton.removeListener(easyButton.getClickListener());
		return easyButton;
	}

	private void buildFourSquareCheckbox(final CortexPreferences preferences, Drawable emptyCheckbox,
			Drawable checkedCheckbox) {
		final ImageButton fourSquareCheckbox = new ImageButton(emptyCheckbox, emptyCheckbox,
				checkedCheckbox);
		fourSquareCheckbox.setBounds(416, Launch.SCREEN_HEIGHT - 593,
				CHECKBOX_LENGTH, CHECKBOX_LENGTH);
		fourSquareCheckbox.setChecked(preferences.isFourSquareEnabled());
		fourSquareCheckbox.left();
		fourSquareCheckbox.bottom();
		fourSquareCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				preferences.setFourSquareEnabled(fourSquareCheckbox.isChecked());
			}
		});
		stage.addActor(fourSquareCheckbox);
	}

	private ImageButton buildHardButton(CortexPreferences preferences, Drawable emptyStar, Drawable fullStar) {
		ImageButton hardButton = new ImageButton(emptyStar, emptyStar, fullStar);
		hardButton.setBounds(351 + (91 * 2), Launch.SCREEN_HEIGHT - 721,
				STAR_WIDTH, STAR_HEIGHT);
		hardButton.removeListener(hardButton.getClickListener());
		return hardButton;
	}

	private ImageButton buildMediumButton(CortexPreferences preferences, Drawable emptyStar, Drawable fullStar) {
		ImageButton mediumButton = new ImageButton(emptyStar, emptyStar, fullStar);
		mediumButton.setBounds(351 + 91, Launch.SCREEN_HEIGHT - 721,
				STAR_WIDTH, STAR_HEIGHT);
		mediumButton.removeListener(mediumButton.getClickListener());
		return mediumButton;
	}

	private void buildMusicCheckbox(final CortexPreferences preferences, Drawable emptyCheckbox,
			Drawable checkedCheckbox) {
		final ImageButton musicCheckbox = new ImageButton(emptyCheckbox, emptyCheckbox,
				checkedCheckbox);
		musicCheckbox.setBounds(416, Launch.SCREEN_HEIGHT - 844,
				CHECKBOX_LENGTH, CHECKBOX_LENGTH);
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

	private void buildPlayButton() {
		Drawable playButtonSkin = skin.getDrawable("play_button");
		final ImageButton playButton = new ImageButton(playButtonSkin);
		playButton.setBounds(277, Launch.SCREEN_HEIGHT - 1045,
				RIGHT_BUTTON_WIDTH, RIGHT_BUTTON_HEIGHT);
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new PlayScreen(game));
			}
		});
		stage.addActor(playButton);
	}

	private void buildQuitButton() {
		Drawable quitButtonSkin = skin.getDrawable("quit_button");
		final ImageButton exitButton = new ImageButton(quitButtonSkin);
		exitButton.setBounds(72, Launch.SCREEN_HEIGHT - 1045,
				LEFT_BUTTON_WIDTH, LEFT_BUTTON_HEIGHT);
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new TitleScreen(game));
			}
		});
		stage.addActor(exitButton);

	}

	private void buildResumeButton() {
		Drawable resumeButtonSkin = skin.getDrawable("resume_button");
		ImageButton resumeButton = new ImageButton(resumeButtonSkin);
		resumeButton.setBounds(277, Launch.SCREEN_HEIGHT - 1045,
				RIGHT_BUTTON_WIDTH, RIGHT_BUTTON_HEIGHT);
//		stage.addActor(resumeButton);
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
