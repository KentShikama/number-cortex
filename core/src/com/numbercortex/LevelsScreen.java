package com.numbercortex;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class LevelsScreen implements Screen {
	
	public static final String TAG = PlayScreen.class.getCanonicalName();
	private static final Skin skin = Assets.levelsSkin;

	private Game game;
	private Stage stage;
	
	private static final String TITLE = "level_selector";
	private static final String BOARD_SIZE_3_LABEL = "3x3";
	private static final String BOARD_SIZE_4_LABEL = "4x4";
	private static final String UNLOCKED_LEVEL = "empty_rectangle";
	private static final String LOCKED_LEVEL = "locked_rectangle";
	
	private static TextButton.TextButtonStyle levelButtonStyle = buildButtonStyle();
	private static TextButton.TextButtonStyle buildButtonStyle() {
		BitmapFont font = FontGenerator.getLevelFont();
		Drawable numberRectangle = skin.getDrawable(UNLOCKED_LEVEL);
		Drawable numberRectangleDisabled = skin.getDrawable(LOCKED_LEVEL);
		TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
		buttonStyle.font = font;
		buttonStyle.fontColor = Launch.BRIGHT_YELLOW;
		buttonStyle.up = numberRectangle;
		buttonStyle.disabled = numberRectangleDisabled;
		return buttonStyle;
	}
	
	private static CortexPreferences preferences = CortexPreferences.getInstance();

	LevelsScreen(Game game) {
		this.game = game;
		stage = ((Launch) game).getStage();
	}
	
	@Override
	public void show() {
		stage.clear();
		
		PlayScreenBackground background = new PlayScreenBackground(Launch.SEA_BLUE);
		stage.addActor(background);
		
		Table table = new Table();
		table.debug();
		table.setWidth(Launch.SCREEN_WIDTH);
		Drawable titleDrawable = skin.getDrawable(TITLE);
		Image title = new Image(titleDrawable);
		table.add(title).expandX().padTop(90).padBottom(60).colspan(3);
		table.row();
		
		Drawable boardSize3LabelDrawable = skin.getDrawable(BOARD_SIZE_3_LABEL);
		Image boardSize3Label = new Image(boardSize3LabelDrawable);
		table.add(boardSize3Label).left().padBottom(20).padTop(20).colspan(2);
		table.row();
		
		TextButton level1 = new TextButton("1", levelButtonStyle);
		TextButton level2 = new TextButton("2", levelButtonStyle);
		TextButton level3 = new TextButton("3", levelButtonStyle);

		table.add(level1).size(100, 100).pad(20).right();
		table.add(level2).size(100, 100).pad(20);
		table.add(level3).size(100, 100).pad(20).left();
		table.row();
		
		TextButton level4 = new TextButton("4", levelButtonStyle);
		TextButton level5 = new TextButton("5", levelButtonStyle);
		TextButton level6 = new TextButton("", levelButtonStyle);
		level6.setDisabled(true);

		table.add(level4).size(100, 100).pad(20).right();
		table.add(level5).size(100, 100).pad(20);
		table.add(level6).size(100, 100).pad(20).left();
		table.row();
		
		TextButton level7 = new TextButton("", levelButtonStyle);
		level7.setDisabled(true);
		TextButton level8 = new TextButton("", levelButtonStyle);
		level8.setDisabled(true);
		TextButton level9 = new TextButton("", levelButtonStyle);
		level9.setDisabled(true);

		table.add(level7).size(100, 100).pad(20).right();
		table.add(level8).size(100, 100).pad(20);
		table.add(level9).size(100, 100).pad(20).left();
		table.row();
	
		Drawable boardSize4LabelDrawable = skin.getDrawable(BOARD_SIZE_4_LABEL);
		Image boardSize4Label = new Image(boardSize4LabelDrawable);
		table.add(boardSize4Label).left().padBottom(20).padTop(20).colspan(2);
		table.row();
		
		TextButton level10 = new TextButton("", levelButtonStyle);
		level10.setDisabled(true);
		TextButton level11 = new TextButton("", levelButtonStyle);
		level11.setDisabled(true);
		TextButton level12 = new TextButton("", levelButtonStyle);
		level12.setDisabled(true);

		table.add(level10).size(100, 100).pad(20).right();
		table.add(level11).size(100, 100).pad(20);
		table.add(level12).size(100, 100).pad(20).left();
		table.row();
		
		TextButton level13 = new TextButton("", levelButtonStyle);
		level13.setDisabled(true);
		TextButton level14 = new TextButton("", levelButtonStyle);
		level14.setDisabled(true);
		TextButton level15 = new TextButton("", levelButtonStyle);
		level15.setDisabled(true);

		table.add(level13).size(100, 100).pad(20).right();
		table.add(level14).size(100, 100).pad(20);
		table.add(level15).size(100, 100).pad(20).left();
		table.row();
		
		TextButton level16 = new TextButton("", levelButtonStyle);
		level16.setDisabled(true);
		TextButton level17 = new TextButton("", levelButtonStyle);
		level17.setDisabled(true);
		TextButton level18 = new TextButton("", levelButtonStyle);
		level18.setDisabled(true);

		table.add(level16).size(100, 100).pad(20).right();
		table.add(level17).size(100, 100).pad(20);
		table.add(level18).size(100, 100).pad(20).left();
		table.row();
		
		ScrollPane pane = new ScrollPane(table);
		pane.setBounds(0, 0, Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		stage.addActor(pane);
	}

	@Override
	public void resume() {
		Assets.loadLevels();
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
	public void dispose() {}

}
