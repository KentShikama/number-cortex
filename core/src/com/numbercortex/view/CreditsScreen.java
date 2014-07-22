package com.numbercortex.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.numbercortex.Persistence;

public class CreditsScreen extends BackCatchingScreen implements Screen {

	public static final String TAG = "Credits Screen";
	private Stage stage;
	private Game game;

	public CreditsScreen(Game game) {
		this.game = game;
	}
	
	@Override
	public void show() {
		FitViewport fitViewport = new FitViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		stage = new Stage(fitViewport);
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(true);
		backKey = false;
		
		buildBottomNavigation(stage);
	}
	private void buildBottomNavigation(Stage stage) {
		Table navigationTable = new Table();
		addIcon(navigationTable);
		addText(navigationTable);
		navigationTable.setBounds(0, 0, 220, 100);
		navigationTable.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(ScreenTracker.moreScreen);
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
		Label buttonLabel = new Label("More", labelStyle);
		table.add(buttonLabel).left().pad(6);
	}

	@Override
	public void pause() {
		Persistence persistence = Persistence.getInstance();
		persistence.setCurrentScreen(TAG);
	}
	
	@Override
	public void render(float delta) {
		handleBackKey();
		stage.act(delta);
		stage.draw();
	}
	private void handleBackKey() {
		if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
			backKey = true;
		} else if (backKey) {
			backKey = false;
			game.setScreen(ScreenTracker.titleScreen);
		}
	}
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	@Override
	public void resume() {}
	@Override
	public void dispose() {}
	@Override
	public void hide() {}

}
