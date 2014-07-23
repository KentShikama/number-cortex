package com.numbercortex.view;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.numbercortex.Persistence;

public class CreditsScreen extends GameScreen {

	public static final String TAG = "Credits Screen";
	private Stage stage;

	private ArrayList<String> contentBlocks = new ArrayList<String>();

	public CreditsScreen(Game game) {
		super(game);
	}

	@Override
	public void show() {
		FitViewport fitViewport = new FitViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		stage = new Stage(fitViewport);
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(true);
		backKey = false;

		buildContentBlocks();

		Table table = new Table();
		Label title = buildTitle(stage);
		table.add(title).center().padBottom(80);
		table.row();

		for (String contentBlock : contentBlocks) {
			Label contentBlockLabel = buildContentBlockLabel(contentBlock);
			table.add(contentBlockLabel).width(480).center().padBottom(40);
			table.row();
		}

		table.setFillParent(true);
		stage.addActor(table);
		buildBottomNavigation(stage);
	}
	private void buildContentBlocks() {
		contentBlocks.clear();
		String kent = "Game Design and Programming:\nKent Shikama";
		String yongchul = "Music:\nYongChul Yoon";
		String libraries = "Libraries:\nLibgdx, Robovm, Freetype, Bfxr";
		contentBlocks.add(kent);
		contentBlocks.add(yongchul);
		contentBlocks.add(libraries);
	}
	private Label buildTitle(Stage stage) {
		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = FontGenerator.getGillSansLight140();
		labelStyle.fontColor = Launch.BRIGHT_YELLOW;
		Label title = new Label("Credits", labelStyle);
		return title;
	}
	private Label buildContentBlockLabel(String contentBlock) {
		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = FontGenerator.getGillSans57();
		labelStyle.fontColor = Launch.BRIGHT_YELLOW;
		Label label = new Label(contentBlock, labelStyle);
		label.setAlignment(Align.center);
		label.setWrap(true);
		return label;
	}
	private void buildBottomNavigation(Stage stage) {
		BackBottomNavigation backBottomNavigation = new BackBottomNavigation("More", ScreenTracker.moreScreen);
		stage.addActor(backBottomNavigation);
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
			game.setScreen(ScreenTracker.moreScreen);
		}
	}
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
}
