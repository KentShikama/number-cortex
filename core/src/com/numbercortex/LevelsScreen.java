package com.numbercortex;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class LevelsScreen implements Screen {
	
	public static final String TAG = PlayScreen.class.getCanonicalName();
	private static final Skin skin = Assets.levelsSkin;

	private Game game;
	private Stage stage;
	private ArrayList<TextButton> textButtons = buildTextButtons(18);
	private ArrayList<ClickListener> listeners = buildClicklisteners(18);
	
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
					game.setScreen(ScreenTracker.playScreen);
				}
			}
		};
		return listener;
	}
	
	private ArrayList<TextButton> buildTextButtons(int numberOfLevels) {
		ArrayList<TextButton> textButtons = new ArrayList<TextButton>();
		for (int i = 0; i < numberOfLevels; i++) {
			textButtons.add(buildTextButton(i + 1));
		}
		return textButtons;
	}
	private TextButton buildTextButton(int level) {
		return new TextButton(String.valueOf(level), levelButtonStyle);
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
		
		TextButton level1 = buildEnabledLevel(1);
		TextButton level2 = buildEnabledLevel(2);
		TextButton level3 = buildEnabledLevel(3);

		table.add(level1).size(100, 100).pad(20).right();
		table.add(level2).size(100, 100).pad(20);
		table.add(level3).size(100, 100).pad(20).left();
		table.row();
		
		TextButton level4 = buildEnabledLevel(4);
		TextButton level5 = buildEnabledLevel(5);
		TextButton level6 = buildEnabledLevel(6);

		table.add(level4).size(100, 100).pad(20).right();
		table.add(level5).size(100, 100).pad(20);
		table.add(level6).size(100, 100).pad(20).left();
		table.row();
		
		TextButton level7 = buildEnabledLevel(7);
		TextButton level8 = buildEnabledLevel(8);
		TextButton level9 = buildEnabledLevel(9);

		table.add(level7).size(100, 100).pad(20).right();
		table.add(level8).size(100, 100).pad(20);
		table.add(level9).size(100, 100).pad(20).left();
		table.row();
	
		Drawable boardSize4LabelDrawable = skin.getDrawable(BOARD_SIZE_4_LABEL);
		Image boardSize4Label = new Image(boardSize4LabelDrawable);
		table.add(boardSize4Label).left().padBottom(20).padTop(20).colspan(2);
		table.row();
		
		TextButton level10 = buildEnabledLevel(10);
		TextButton level11 = buildEnabledLevel(11);
		TextButton level12 = buildEnabledLevel(12);

		table.add(level10).size(100, 100).pad(20).right();
		table.add(level11).size(100, 100).pad(20);
		table.add(level12).size(100, 100).pad(20).left();
		table.row();
		
		TextButton level13 = buildEnabledLevel(13);
		TextButton level14 = buildEnabledLevel(14);
		TextButton level15 = buildEnabledLevel(15);

		table.add(level13).size(100, 100).pad(20).right();
		table.add(level14).size(100, 100).pad(20);
		table.add(level15).size(100, 100).pad(20).left();
		table.row();
		
		TextButton level16 = buildEnabledLevel(16);
		TextButton level17 = buildEnabledLevel(17);
		TextButton level18 = buildDisabledLevel(18);
		
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
