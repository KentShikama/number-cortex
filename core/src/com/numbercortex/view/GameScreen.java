package com.numbercortex.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameScreen implements Screen {
	boolean backKey;
	Game game;
	
	private enum Direction {
		LEFT, RIGHT;
	}
	private class BottomNavigation extends Group {
		BottomNavigation(Direction direction, String previousScreenName, final GameScreen previousScreen) {
			addContent(direction, previousScreenName);
			addArmature(previousScreen);
		}	
		private void addContent(Direction direction, String previousScreenName) {
			Table navigationTable = new Table();
			addIcon(navigationTable);
			addText(previousScreenName, navigationTable);
			if (direction == Direction.LEFT) {
				navigationTable.setBounds(0, 0, 220, 100);
			} else {
				navigationTable.setBounds(Launch.SCREEN_WIDTH - 220, 0, 220, 100);
			}
			this.addActor(navigationTable);
		}
		private void addIcon(Table table) {
			TextureRegion buttonIconTexture = Assets.homeSkin.getRegion("left_arrow");
			Image buttonIcon = new Image(buttonIconTexture);
			table.add(buttonIcon).center().pad(6).padBottom(10);
		}
		private void addText(String text, Table table) {
			Label.LabelStyle labelStyle = new Label.LabelStyle();
			labelStyle.font = FontGenerator.getGillSans50();
			labelStyle.fontColor = Launch.BRIGHT_YELLOW;
			Label buttonLabel = new Label(text, labelStyle);
			table.add(buttonLabel).left().pad(6).padBottom(10);
		}
		private void addArmature(final GameScreen previousScreen) {
			Image buttonBackground = new Image();
			buttonBackground.setBounds(0, 0, 220, 100);
			buttonBackground.addListener(new ClickListenerWithSound() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					game.setScreen(previousScreen);
				}
			});
			this.addActor(buttonBackground);
		}
	}
	class BackBottomNavigation extends BottomNavigation {
		BackBottomNavigation(String previousScreenName, final GameScreen previousScreen) {
			super(Direction.LEFT, previousScreenName, previousScreen);
		}	
	}
	class ForwardBottomNavigation extends BottomNavigation {
		ForwardBottomNavigation(String previousScreenName, GameScreen previousScreen) {
			super(Direction.RIGHT, previousScreenName, previousScreen);
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
