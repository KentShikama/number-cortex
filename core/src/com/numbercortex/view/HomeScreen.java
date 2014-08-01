package com.numbercortex.view;

import libgdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.numbercortex.ModeTracker;
import com.numbercortex.Persistence;
import com.numbercortex.logic.GameManager;
import com.numbercortex.logic.SinglePlayerGameManager;
import com.numbercortex.view.TransitionScreen.Direction;

abstract class HomeScreen extends GameScreen {
	class HomeScreenButton extends Group {
		HomeScreenButton(String buttonName, int index, Screen screen, ModeTracker.Mode mode) {
			this(buttonName, index, new DefaultHomeScreenListener(screen, mode));
		}
		HomeScreenButton(String buttonName, int index, ClickListener listener) {
			TextureRegion buttonBoxTexture = Assets.homeSkin.getRegion("button_box");
			addContent(buttonName, index, buttonBoxTexture);
			addArmature(listener, index, buttonBoxTexture);
		}
		private void addContent(String buttonName, int index, TextureRegion buttonBoxTexture) {
			Table buttonTable = new Table();
			addIcon(buttonName, buttonTable);
			addText(buttonName, buttonTable);
			buttonTable.setBounds(175, Launch.SCREEN_HEIGHT - (FIRST_BUTTON_CENTER_OFFSET_Y + index * 80),
					buttonBoxTexture.getRegionWidth(), buttonBoxTexture.getRegionHeight());
			this.addActor(buttonTable);
		}
		private void addIcon(String buttonName, Table buttonTable) {
			String buttonIconName = buttonName.toLowerCase().replace(" ", "_") + "_icon";
			TextureRegion buttonIconTexture = Assets.homeSkin.getRegion(buttonIconName);
			Image buttonIcon = new Image(buttonIconTexture);
			buttonTable.add(buttonIcon).right().pad(6);
		}
		private void addText(String buttonName, Table buttonTable) {
			Label.LabelStyle labelStyle = new Label.LabelStyle();
			labelStyle.font = Assets.gillSans41;
			labelStyle.fontColor = Launch.BRIGHT_YELLOW;
			Label buttonLabel = new Label(buttonName, labelStyle);
			buttonTable.add(buttonLabel).center().pad(6);
		}
		private void addArmature(ClickListener listener, int index, TextureRegion buttonBoxTexture) {
			Image buttonBackground = new Image(buttonBoxTexture);
			buttonBackground.setBounds(175, Launch.SCREEN_HEIGHT - (FIRST_BUTTON_CENTER_OFFSET_Y + index * 80),
					buttonBoxTexture.getRegionWidth(), buttonBoxTexture.getRegionHeight());
			this.addActor(buttonBackground);
			if (listener != null) {
				buttonBackground.addListener(listener);
			}
		}
	}
	class DefaultHomeScreenListener extends ClickListenerWithSound {
		private Screen screen;
		private ModeTracker.Mode mode;
		DefaultHomeScreenListener(Screen screen, ModeTracker.Mode mode) {
			this.screen = screen;
			this.mode = mode;
		}
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if (screen != null) {
				if (isFirstTimePlaying()) {
					startFirstGame();
				} else {
					ModeTracker.mode = mode;
					ScreenTracker.transitionScreen.transition(Direction.RIGHT, (GameScreen) screen);
				}
			}
		}
		private boolean isFirstTimePlaying() {
			return mode == ModeTracker.Mode.SINGLE_PLAYER && Persistence.getInstance().getMaxLevel() == 0;
		}
		private void startFirstGame() {
			ModeTracker.mode = mode;
			GameManager manager = SinglePlayerGameManager.createNewGameManager(ScreenTracker.playScreen);
			ScreenTracker.transitionScreen.transition(Direction.RIGHT, ScreenTracker.playScreen);
			manager.startNewGame();
		}
	}

	private static final String TITLE = "number_cortex_title";
	private static final String LINE_EXTENSION = "line_extension";

	private static final int FIRST_BUTTON_CENTER_OFFSET_Y = 734;

	HomeScreen(Game game) {
		super(game);
	}

	@Override
	public void show() {
		FitViewport fitViewport = new FitViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		stage = new Stage(fitViewport);
		Gdx.input.setInputProcessor(stage);
		setUpBackKeyCatch();
		buildTitle(stage);
		buildButtons(stage);
		buildLineExtension(stage);
		buildBottomNavigation(stage);
	}
	abstract void setUpBackKeyCatch();
	private void buildTitle(Stage stage) {
		TextureRegion titleTexture = Assets.homeSkin.getRegion(TITLE);
		Image title = new Image(titleTexture);
		title.setBounds(63, Launch.SCREEN_HEIGHT - (FIRST_BUTTON_CENTER_OFFSET_Y - 74), titleTexture.getRegionWidth(),
				titleTexture.getRegionHeight());
		stage.addActor(title);
	}
	abstract void buildButtons(Stage stage);
	private void buildLineExtension(Stage stage) {
		TextureRegion lineExtensionTexture = Assets.homeSkin.getRegion(LINE_EXTENSION);
		Image lineExtension = new Image(lineExtensionTexture);
		lineExtension.setBounds(175, Launch.SCREEN_HEIGHT - (FIRST_BUTTON_CENTER_OFFSET_Y + 280),
				lineExtensionTexture.getRegionWidth(), lineExtensionTexture.getRegionHeight());
		stage.addActor(lineExtension);
	}
	abstract void buildBottomNavigation(Stage stage);

	@Override
	public void render(float delta) {
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		handleBackKey();
		stage.act(delta);
		stage.draw();
	}
	abstract void handleBackKey();

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
}
