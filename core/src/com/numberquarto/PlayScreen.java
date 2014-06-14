package com.numberquarto;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class PlayScreen implements Screen {
	
	private Game game;
	private Stage stage;
	
	private static final Skin skin = Assets.gameSkin;
	private static final String RED_BACKGROUND = "red_background";
	private static final String BLUE_BACKGROUND = "blue_background";
	
	private static final int BOTTOM_RECTANGLE_WIDTH = skin.getRegion("settings").getRegionWidth();
	private static final int BOTTOM_RECTANGLE_HEIGHT = skin.getRegion("settings").getRegionHeight();
	
	PlayScreen(Game game) {
		this.game = game;
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
		stage = new Stage(new FitViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT));
		Gdx.input.setInputProcessor(stage);

		ScreenBackground background = new ScreenBackground(skin, RED_BACKGROUND);
		stage.addActor(background);
		
		buildNumberScroller();
		
		Drawable settingsRectangleSkin = skin.getDrawable("settings");
		Drawable helpRectangleSkin = skin.getDrawable("help");

		buildSettingsButton(settingsRectangleSkin);	
		buildHelpButton(helpRectangleSkin);
	}
	
	class NumberScroller {
		Stage stage;
		ScrollPane numberScroller;
		ScrollPane.ScrollPaneStyle style;
		Table numberTable;
		
		TextButton.TextButtonStyle buttonStyle;
		
		FreeTypeFontGenerator generator;
		BitmapFont font;
		public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";

		NumberScroller (Stage stage) {
			this.stage = stage;
			
			numberTable = new Table();
			
			style = new ScrollPane.ScrollPaneStyle();
			style.background = skin.getDrawable("scroller_rectangle");
			numberScroller = new ScrollPane(numberTable, style);
			numberScroller.setBounds(0, Launch.SCREEN_HEIGHT - 1013, 460, 100);
			
			float scale = 0.08f;
			generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Tahoma.ttf"));
			int fontSize = (int) (scale * Gdx.graphics.getHeight());
			System.out.println("screen height: " + Gdx.graphics.getHeight());
			System.out.println("font size: " + fontSize);
			font = generator.generateFont(fontSize);
			font.setColor(255, 255, 0, 1);
			font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			generator.dispose();
			
			buttonStyle = new TextButton.TextButtonStyle(null, null, null, font);
			stage.addActor(numberScroller);

		}
		
		public void update (ArrayList<Integer> numberList) {
			for (Integer number : numberList) {
				numberTable.add(new TextButton(number.toString(), buttonStyle));
			}
		}
	}
	
	private void buildNumberScroller() {
		NumberScroller numberScroller = new NumberScroller(stage);
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 100; i++) {
			list.add(i);
		}
		numberScroller.update(list);
	}

	private void buildSettingsButton(Drawable settingsRectangleSkin) {
		ImageButton settingsButton = new ImageButton(settingsRectangleSkin, settingsRectangleSkin,
				settingsRectangleSkin);
		settingsButton.setBounds(434, Launch.SCREEN_HEIGHT - 1136,
				BOTTOM_RECTANGLE_WIDTH, BOTTOM_RECTANGLE_HEIGHT);
		settingsButton.addListener(new ClickListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new SettingsScreen(game));
				return true;
			}
		});
		stage.addActor(settingsButton);
	}
	
	private void buildHelpButton(Drawable helpRectangleSkin) {
		ImageButton helpButton = new ImageButton(helpRectangleSkin, helpRectangleSkin,
				helpRectangleSkin);
		helpButton.setBounds(543, Launch.SCREEN_HEIGHT - 1136,
				BOTTOM_RECTANGLE_WIDTH, BOTTOM_RECTANGLE_HEIGHT);
		stage.addActor(helpButton);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
