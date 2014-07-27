package com.numbercortex.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameScreen implements Screen {

	boolean backKey;
	Game game;

	private abstract class BottomNavigation extends Group {
		BottomNavigation(String previousScreenName, final GameScreen previousScreen) {
			this(previousScreenName, new ClickListenerWithSound() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					game.setScreen(previousScreen);
				}
			});
		}
		BottomNavigation(String previousScreenName, ClickListener listener) {
			addContent(previousScreenName);
			addArmature(listener);
		}
		abstract void setBounds(Actor navigationTable);
		abstract TextureRegion buildFlippedIconTexture(TextureRegion buttonIconTexture);
		abstract void addContentsToTable(Table navigationTable, Image buttonIcon, Label buttonLabel);
		private void addContent(String previousScreenName) {
			Table navigationTable = new Table();
			Image buttonIcon = addIcon();
			Label buttonLabel = addText(previousScreenName);
			addContentsToTable(navigationTable, buttonIcon, buttonLabel);
			setBounds(navigationTable);
			this.addActor(navigationTable);
		}
		private Image addIcon() {
			TextureRegion buttonIconTexture = Assets.homeSkin.getRegion("left_arrow");
			TextureRegion flippedIconTexture = buildFlippedIconTexture(buttonIconTexture);
			Image buttonIcon = new Image(flippedIconTexture);
			return buttonIcon;
		}
		private Label addText(String text) {
			Label.LabelStyle labelStyle = new Label.LabelStyle();
			labelStyle.font = FontGenerator.getGillSans50();
			labelStyle.fontColor = Launch.BRIGHT_YELLOW;
			Label buttonLabel = new Label(text, labelStyle);
			return buttonLabel;
		}
		private void addArmature(ClickListener listener) {
			Image buttonBackground = new Image();
			setBounds(buttonBackground);
			buttonBackground.addListener(listener);
			this.addActor(buttonBackground);
		}
	}
	class BackBottomNavigation extends BottomNavigation {
		BackBottomNavigation(String previousScreenName, final GameScreen previousScreen) {
			super(previousScreenName, previousScreen);
		}
		BackBottomNavigation(String previousScreenName, ClickListener listener) {
			super(previousScreenName, listener);
		}
		@Override
		void setBounds(Actor actor) {
			actor.setBounds(0, 0, 220, 100);
		}
		@Override
		TextureRegion buildFlippedIconTexture(TextureRegion buttonIconTexture) {
			TextureRegion flippedTexture = new TextureRegion(buttonIconTexture, 0, 0,
					buttonIconTexture.getRegionWidth(), buttonIconTexture.getRegionHeight());
			return flippedTexture;
		}
		@Override
		void addContentsToTable(Table navigationTable, Image buttonIcon, Label buttonLabel) {
			navigationTable.add(buttonIcon).center().pad(6).padBottom(10);
			navigationTable.add(buttonLabel).left().pad(6).padBottom(10);
		}
	}
	class ForwardBottomNavigation extends BottomNavigation {
		ForwardBottomNavigation(String previousScreenName, GameScreen previousScreen) {
			super(previousScreenName, previousScreen);
		}
		ForwardBottomNavigation(String previousScreenName, ClickListener listener) {
			super(previousScreenName, listener);
		}
		@Override
		void setBounds(Actor actor) {
			actor.setBounds(Launch.SCREEN_WIDTH - 220, 0, 220, 100);
		}
		@Override
		TextureRegion buildFlippedIconTexture(TextureRegion buttonIconTexture) {
			TextureRegion flippedTexture = new TextureRegion(buttonIconTexture, buttonIconTexture.getRegionWidth(), 0,
					-buttonIconTexture.getRegionWidth(), buttonIconTexture.getRegionHeight());
			return flippedTexture;
		}
		@Override
		void addContentsToTable(Table navigationTable, Image buttonIcon, Label buttonLabel) {
			navigationTable.add(buttonLabel).right().pad(6).padBottom(10);
			navigationTable.add(buttonIcon).center().pad(6).padBottom(10);
		}
	}

	GameScreen(Game game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {}
	@Override
	public void resize(int width, int height) {}
	@Override
	public void show() {}
	@Override
	public void hide() {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}
	@Override
	public void dispose() {}
}
