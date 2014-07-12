package com.numbercortex;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class LevelsScreen implements Screen {

	public static final String TAG = PlayScreen.class.getCanonicalName();

	private Game game;
	private Stage stage;
	private Table table = new Table();

	private static TextButton.TextButtonStyle levelButtonStyle = buildButtonStyle();
	private static TextButton.TextButtonStyle buildButtonStyle() {
		BitmapFont font = FontGenerator.getLevelNumberFont();
		Drawable numberRectangle = Assets.levelsSkin.getDrawable(UNLOCKED_LEVEL);
		Drawable numberRectangleDisabled = Assets.levelsSkin.getDrawable(LOCKED_LEVEL);
		TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
		buttonStyle.font = font;
		buttonStyle.fontColor = Launch.BRIGHT_YELLOW;
		buttonStyle.up = numberRectangle;
		buttonStyle.disabled = numberRectangleDisabled;
		return buttonStyle;
	}

	private ArrayList<TextButton> textButtons = buildTextButtons(18);
	private ArrayList<TextButton> buildTextButtons(int numberOfLevels) {
		ArrayList<TextButton> textButtons = new ArrayList<TextButton>();
		for (int i = 0; i < numberOfLevels; i++) {
			textButtons.add(buildTextButton(i + 1));
		}
		return textButtons;
	}
	private TextButton buildTextButton(int level) {
		if (levelButtonStyle == null) {
			levelButtonStyle = buildButtonStyle();
		}
		return new TextButton(String.valueOf(level), levelButtonStyle);
	}

	private ArrayList<ClickListener> listeners = buildClicklisteners(18);
	private ArrayList<ClickListener> buildClicklisteners(int numberOfLevels) {
		ArrayList<ClickListener> listeners = new ArrayList<ClickListener>();
		for (int i = 0; i < numberOfLevels; i++) {
			listeners.add(buildClickListener(i + 1));
		}
		return listeners;
	}
	private ClickListener buildClickListener(final int level) {
		ClickListener listener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				TextButton button = (TextButton) event.getListenerActor();
				if (!button.isDisabled()) {
					ScreenTracker.level = level;
					game.setScreen(ScreenTracker.singlePlayerSettingsScreen);
				}
			}
		};
		return listener;
	}

	private static final String TITLE = "level_selector";
	private static final String BOARD_SIZE_3_LABEL = "3x3";
	private static final String BOARD_SIZE_4_LABEL = "4x4";
	private static final String UNLOCKED_LEVEL = "empty_rectangle";
	private static final String LOCKED_LEVEL = "locked_rectangle";

	private static CortexPreferences preferences = CortexPreferences.getInstance();

	LevelsScreen(Game game) {
		this.game = game;
		stage = ((Launch) game).getStage();
	}

	@Override
	public void show() {
		stage.clear();
		table.clear();

		addBackground();
		addTitle();
		addBoardSizeLabel(BOARD_SIZE_3_LABEL);
		addLevelButtons(1, 9);
		addBoardSizeLabel(BOARD_SIZE_4_LABEL);
		addLevelButtons(10, 18);
		addBackButton();

		addScrollPane();
	}
	private void addBackground() {
		BackgroundScreen background = new BackgroundScreen(Launch.SEA_BLUE);
		stage.addActor(background);
	}
	private void addTitle() {
		Drawable titleDrawable = Assets.levelsSkin.getDrawable(TITLE);
		Image title = new Image(titleDrawable);
		table.add(title).expandX().padTop(90).padBottom(60).colspan(3);
		table.row();
	}
	private void addBoardSizeLabel(String boardSizeLabelString) {
		Drawable boardSize3LabelDrawable = Assets.levelsSkin.getDrawable(boardSizeLabelString);
		Image boardSize3Label = new Image(boardSize3LabelDrawable);
		table.add(boardSize3Label).left().padBottom(20).padTop(20).colspan(2);
		table.row();
	}
	private void addLevelButtons(int startingLevel, int endingLevel) {
		for (int i = startingLevel - 1; i < endingLevel; i++) {
			int level = i + 1;
			TextButton levelButton;
			if (level <= preferences.getCurrentLevel()) {
				levelButton = buildEnabledLevel(level);
			} else {
				levelButton = buildDisabledLevel(level);
			}
			if (level % 3 == 1) {
				table.add(levelButton).size(100, 100).pad(20).right();
			} else if (level % 3 == 2) {
				table.add(levelButton).size(100, 100).pad(20);
			} else {
				table.add(levelButton).size(100, 100).pad(20).left();
				table.row();
			}
		}
	}
	private TextButton buildEnabledLevel(final int level) {
		TextButton levelButton = textButtons.get(level - 1);
		levelButton.setText(String.valueOf(level));
		levelButton.setDisabled(false);
		ClickListener listener = listeners.get(level - 1);
		levelButton.addListener(listener);
		return levelButton;
	}
	private TextButton buildDisabledLevel(final int level) {
		TextButton levelButton = textButtons.get(level - 1);
		levelButton.setText("");
		levelButton.setDisabled(true);
		return levelButton;
	}
	private void addBackButton() {
		TextureRegion backButtonTexture = Assets.levelsSkin.getRegion("back_button");
		Image backButton = new Image(backButtonTexture);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(ScreenTracker.titleScreen);
			}
		});
		table.add();
		table.add(backButton).padTop(30).padBottom(30);
		table.row();
	}
	private void addScrollPane() {
		ScrollPane.ScrollPaneStyle style = buildScrollPaneStyle();
		ScrollPane pane = new ScrollPane(table, style);
		pane.setBounds(0, 0, Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		stage.addActor(pane);
	}
	private ScrollPane.ScrollPaneStyle buildScrollPaneStyle() {
		TextureRegion background = new TextureRegion(Assets.backgroundTexture);
		Drawable backgroundDrawable = new TextureRegionDrawable(background);
		ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
		style.background = backgroundDrawable;
		return style;
	}

	@Override
	public void resume() {
		if (FontGenerator.isNull()) {
			FontGenerator.load();
		}
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
	public void pause() {}
	@Override
	public void hide() {}
	@Override
	public void dispose() {
		levelButtonStyle = null;
	}

}
