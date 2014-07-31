package com.numbercortex.view;

import libgdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.numbercortex.view.TransitionScreen.Direction;

class GameScreen implements Screen {

	boolean backKey;
	Game game;
	Stage stage;

	private abstract class BottomNavigation extends Group {

		private static final String NAVIGATION_ARROW_ICON = "left_arrow";

		BottomNavigation(String screenName, final GameScreen screen, final Direction direction) {
			this(screenName, new ClickListenerWithSound() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					ScreenTracker.transitionScreen.transition(direction, screen);
				}
			});
		}
		BottomNavigation(String screenName, ClickListener listener) {
			addContent(screenName);
			addArmature(listener);
		}
		abstract void setBounds(Actor navigationTable);
		abstract TextureRegion buildFlippedIconTexture(TextureRegion buttonIconTexture);
		abstract void addContentsToTable(Table navigationTable, Image buttonIcon, Label buttonLabel);
		private void addContent(String screenName) {
			Table navigationTable = new Table();
			Image buttonIcon = addIcon();
			Label buttonLabel = addText(screenName);
			addContentsToTable(navigationTable, buttonIcon, buttonLabel);
			setBounds(navigationTable);
			this.addActor(navigationTable);
		}
		private Image addIcon() {
			TextureRegion buttonIconTexture = Assets.homeSkin.getRegion(NAVIGATION_ARROW_ICON);
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
			super(previousScreenName, previousScreen, Direction.LEFT);
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
		ForwardBottomNavigation(String nextScreenName, GameScreen nextScreen) {
			super(nextScreenName, nextScreen, Direction.RIGHT);
		}
		ForwardBottomNavigation(String nextScreenName, ClickListener listener) {
			super(nextScreenName, listener);
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
