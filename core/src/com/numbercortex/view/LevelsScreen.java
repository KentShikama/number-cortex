package com.numbercortex.view;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.numbercortex.ModeTracker;
import com.numbercortex.Persistence;
import com.numbercortex.view.TransitionScreen.Direction;

class LevelsScreen extends GameScreen {

	public static final String TAG = "Levels Screen";

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

	private ArrayList<ClickListenerWithSound> listeners = buildClickListenerWithSounds(18);
	private ArrayList<ClickListenerWithSound> buildClickListenerWithSounds(int numberOfLevels) {
		ArrayList<ClickListenerWithSound> listeners = new ArrayList<ClickListenerWithSound>();
		for (int i = 0; i < numberOfLevels; i++) {
			listeners.add(buildClickListenerWithSound(i + 1));
		}
		return listeners;
	}
	private ClickListenerWithSound buildClickListenerWithSound(final int level) {
		ClickListenerWithSound listener = new ClickListenerWithSound() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				TextButton button = (TextButton) event.getListenerActor();
				if (!button.isDisabled()) {
					Persistence persistence = Persistence.getInstance();
					persistence.setCurrentLevel(level);
					ScreenTracker.transitionScreen
							.transition(Direction.RIGHT, ScreenTracker.singlePlayerSettingsScreen);
				}
			}
		};
		return listener;
	}

	private static final String BOARD_SIZE_3_LABEL = "3x3";
	private static final String BOARD_SIZE_4_LABEL = "4x4";
	private static final String UNLOCKED_LEVEL = "empty_rectangle";
	private static final String LOCKED_LEVEL = "locked_rectangle";

	private static Persistence preferences = Persistence.getInstance();

	LevelsScreen(Game game) {
		super(game);
	}

	@Override
	public void show() {
		FitViewport fitViewport = new FitViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		stage = new Stage(fitViewport);
		Gdx.input.setInputProcessor(stage);

		table.clear();

		addTitle();
		addBoardSizeLabel(BOARD_SIZE_3_LABEL);
		addLevelButtons(1, 9);
		addBoardSizeLabel(BOARD_SIZE_4_LABEL);
		addLevelButtons(10, 18);
		addBackButton();

		addScrollPane();

		Gdx.input.setCatchBackKey(true);
		backKey = false;
	}
	private void addTitle() {
		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = FontGenerator.getGillSansLight140();
		labelStyle.fontColor = Launch.BRIGHT_YELLOW;
		Label title = new Label("Levels", labelStyle);

		TextureRegion levelsIconTexture = Assets.levelsSkin.getRegion("levels_icon");
		Image levelsIcon = new Image(levelsIconTexture);

		Table titleTable = new Table();
		titleTable.add(title).right().pad(10);
		titleTable.add(levelsIcon).left().pad(10);

		table.add(titleTable).expandX().padTop(80).padBottom(22).colspan(3);
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
			if (level <= preferences.getMaxLevel()) {
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
		ClickListenerWithSound listener = listeners.get(level - 1);
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
		BackBottomNavigation backBottomNavigation = new BackBottomNavigation("Home", ScreenTracker.titleScreen);
		backBottomNavigation.setSize(220, 100);
		table.add(backBottomNavigation).colspan(2).left().padLeft(6).padTop(14).padBottom(16);
	}
	private void addScrollPane() {
		ScrollPane pane = new ScrollPane(table);
		pane.setBounds(0, 0, Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		stage.addActor(pane);
	}
	@Override
	public void pause() {
		Persistence persistence = Persistence.getInstance();
		persistence.setCurrentScreen(TAG);
		persistence.setMode(ModeTracker.mode.name());
	}
	@Override
	public void dispose() {
		levelButtonStyle = null;
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
			ScreenTracker.transitionScreen.transition(Direction.LEFT, ScreenTracker.titleScreen);
		}
	}
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
}
