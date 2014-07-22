package com.numbercortex.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.numbercortex.ModeTracker;
import com.numbercortex.Persistence;
import com.numbercortex.logic.GameManager;
import com.numbercortex.logic.SinglePlayerGameManager;

class TitleScreen implements Screen {
	class TitleScreenButton extends Group {
		private Screen screen;
		private ModeTracker.Mode mode;

		TitleScreenButton(String buttonName, int index, Screen screen, ModeTracker.Mode mode) {
			this.screen = screen;
			this.mode = mode;

			TextureRegion buttonBoxTexture = Assets.homeSkin.getRegion("button_box");
			addArmature(index, screen, buttonBoxTexture);
			addContent(buttonName, index, buttonBoxTexture);
		}
		private void addArmature(int index, Screen screen, TextureRegion buttonBoxTexture) {
			Image buttonBackground = new Image(buttonBoxTexture);
			buttonBackground.setBounds(175, Launch.SCREEN_HEIGHT - (BUTTON_STARTING_Y_COORDINATE + index * 80),
					buttonBoxTexture.getRegionWidth(), buttonBoxTexture.getRegionHeight());
			this.addActor(buttonBackground);
			if (screen != null) {
				ClickListenerWithSound listener = new TitleScreenListener();
				buttonBackground.addListener(listener);
			}
		}
		private void addContent(String buttonName, int index, TextureRegion buttonBoxTexture) {
			Table buttonTable = new Table();
			addIcon(buttonName, buttonTable);
			addText(buttonName, buttonTable);
			buttonTable.setBounds(175, Launch.SCREEN_HEIGHT - (BUTTON_STARTING_Y_COORDINATE + index * 80),
					buttonBoxTexture.getRegionWidth(), buttonBoxTexture.getRegionHeight());
			this.addActor(buttonTable);
		}
		private void addIcon(String buttonName, Table buttonTable) {
			String buttonIconName = buttonName.toLowerCase().replace(" ", "_").replace("&", "and") + "_icon";
			TextureRegion buttonIconTexture = Assets.homeSkin.getRegion(buttonIconName);
			Image buttonIcon = new Image(buttonIconTexture);
			buttonTable.add(buttonIcon).right().pad(6);
		}
		private void addText(String buttonName, Table buttonTable) {
			Label.LabelStyle labelStyle = new Label.LabelStyle();
			labelStyle.font = FontGenerator.getGillSans40();
			labelStyle.fontColor = Launch.BRIGHT_YELLOW;
			Label buttonLabel = new Label(buttonName, labelStyle);
			buttonTable.add(buttonLabel).center().pad(6);
		}

		class TitleScreenListener extends ClickListenerWithSound {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (screen != null) {
					if (mode == ModeTracker.Mode.SINGLE_PLAYER && Persistence.getInstance().getMaxLevel() == 0) {
						ModeTracker.mode = mode;
						GameManager manager = SinglePlayerGameManager.createNewGameManager();
						game.setScreen(ScreenTracker.playScreen);
						manager.startNewGame();
					} else {
						ModeTracker.mode = mode;
						game.setScreen(screen);
					}
				}
			}
		}
	}

	public static final String TAG = "Title Screen";

	private static final String TITLE = "number_cortex_title";
	private static final String PLAY_BUTTON = "Play";
	private static final String PASS_AND_PLAY_BUTTON = "Pass & Play";
	private static final String OPTIONS = "Options";
	private static final String MORE = "More";
	private static final String LINE_EXTENSION = "line_extension";
	
	private static final int BUTTON_STARTING_Y_COORDINATE = 734;

	private Game game;
	private Stage mainStage;

	TitleScreen(Game game) {
		this.game = game;
	}

	@Override
	public void show() {
		FitViewport fitViewport = new FitViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		mainStage = new Stage(fitViewport);
		Gdx.input.setInputProcessor(mainStage);

		buildTitle();
		buildButtons();
		buildLineExtension();
		Gdx.input.setCatchBackKey(false);
	}
	private void buildTitle() {
		TextureRegion titleTexture = Assets.homeSkin.getRegion(TITLE);
		Image title = new Image(titleTexture);
		title.setBounds(63, Launch.SCREEN_HEIGHT - 660, titleTexture.getRegionWidth(),
				titleTexture.getRegionHeight());
		mainStage.addActor(title);
	}
	private void buildButtons() {
		TitleScreenButton playButton = new TitleScreenButton(PLAY_BUTTON, 0, ScreenTracker.levelsScreen,
				ModeTracker.Mode.SINGLE_PLAYER);
		TitleScreenButton passAndPlayButton = new TitleScreenButton(PASS_AND_PLAY_BUTTON, 1,
				ScreenTracker.twoPlayerSettingsScreen, ModeTracker.Mode.TWO_PLAYER);
		TitleScreenButton optionsButton = new TitleScreenButton(OPTIONS, 2, null, null);
		TitleScreenButton moreButton = new TitleScreenButton(MORE, 3, null, null);
		mainStage.addActor(playButton);
		mainStage.addActor(passAndPlayButton);
		mainStage.addActor(optionsButton);
		mainStage.addActor(moreButton);
	}
	private void buildLineExtension() {
		TextureRegion lineExtensionTexture = Assets.homeSkin.getRegion(LINE_EXTENSION);
		Image lineExtension = new Image(lineExtensionTexture);
		lineExtension.setBounds(175, Launch.SCREEN_HEIGHT - (BUTTON_STARTING_Y_COORDINATE + 280), lineExtensionTexture.getRegionWidth(),
				lineExtensionTexture.getRegionHeight());
		mainStage.addActor(lineExtension);
	}

	@Override
	public void render(float delta) {
		mainStage.act(delta);
		mainStage.draw();
	}
	@Override
	public void resize(int width, int height) {
		mainStage.getViewport().update(width, height, true);
	}
	@Override
	public void pause() {
		Persistence persistence = Persistence.getInstance();
		persistence.setCurrentScreen(TAG);
	}
	@Override
	public void resume() {}
	@Override
	public void dispose() {}
	@Override
	public void hide() {}
}
