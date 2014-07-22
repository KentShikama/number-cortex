package com.numbercortex.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.numbercortex.Persistence;

public class MoreScreen extends HomeScreen {

	public static final String TAG = "More Screen";

	private static final String MORE_GAMES_BUTTON = "More Games";
	private static final String RATE_GAME_BUTTON = "Rate Game";
	private static final String WEBSITE_BUTTON = "Website";
	private static final String CREDITS = "Credits";

	MoreScreen(Game game) {
		super(game);
	}

	@Override
	void buildButtons(Stage stage) {
		HomeScreenButton playButton = new HomeScreenButton(MORE_GAMES_BUTTON, 0, null, null);
		HomeScreenButton passAndPlayButton = new HomeScreenButton(RATE_GAME_BUTTON, 1, null, null);
		HomeScreenButton optionsButton = new HomeScreenButton(WEBSITE_BUTTON, 2, null, null);
		HomeScreenButton moreButton = new HomeScreenButton(CREDITS, 3, ScreenTracker.creditsScreen, null);
		stage.addActor(playButton);
		stage.addActor(passAndPlayButton);
		stage.addActor(optionsButton);
		stage.addActor(moreButton);
	}
	@Override
	void buildBottomNavigation(Stage stage) {
		Table navigationTable = new Table();
		addIcon(navigationTable);
		addText(navigationTable);
		navigationTable.setBounds(0, 0, 220, 100);
		navigationTable.addListener(new ClickListenerWithSound() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(ScreenTracker.titleScreen);
			}
		});
		stage.addActor(navigationTable);
	}
	private void addIcon(Table table) {
		TextureRegion buttonIconTexture = Assets.homeSkin.getRegion("left_arrow");
		Image buttonIcon = new Image(buttonIconTexture);
		table.add(buttonIcon).center().pad(6);
	}
	private void addText(Table table) {
		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = FontGenerator.getGillSans40();
		labelStyle.fontColor = Launch.BRIGHT_YELLOW;
		Label buttonLabel = new Label("Home", labelStyle);
		table.add(buttonLabel).left().pad(6);
	}

	@Override
	public void pause() {
		Persistence persistence = Persistence.getInstance();
		persistence.setCurrentScreen(TAG);
	}

}
