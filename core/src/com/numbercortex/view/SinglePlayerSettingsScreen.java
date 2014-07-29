package com.numbercortex.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.numbercortex.CortexState;
import com.numbercortex.GameSettingsLoader;
import com.numbercortex.ModeTracker;
import com.numbercortex.Persistence;
import com.numbercortex.logic.GameManager;
import com.numbercortex.logic.SinglePlayerGameManager;
import com.numbercortex.view.TransitionScreen.Direction;

class SinglePlayerSettingsScreen extends SettingsScreen {

	public static final String TAG = "Single Player Settings Screen";

	private class DifficultyGroup extends SettingGroup {
		private DifficultyGroup(Label label, final StarGroup starGroup, final GroupState groupState) {
			super(groupState);
			SnapshotArray<Actor> starButtons = starGroup.getChildren();
			for (Actor starButton : starButtons) {
				starButton.addListener(new ClickListenerWithSound() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if (groupState == GroupState.CLICKABLE) {
							starGroup.toggleRating();
							gameSettings.setDifficulty(starGroup.rating);
						}
					}
				});
			}
			this.addActor(label);
			this.addActor(starGroup);
		}
		@Override
		public void draw(Batch batch, float parentAlpha) {
			if (groupState == GroupState.TRANSPARENT) {
				parentAlpha = 0.5f;
			}
			SnapshotArray<Actor> children = this.getChildren();
			for (Actor child : children) {
				child.draw(batch, parentAlpha);
			}
		}
	}
	private class StarGroup extends Group {
		private int rating;
		private StarGroup(int startPositionX, int startPositionY, int rating) {
			this(startPositionX, startPositionY, 68, rating, 5);
		}
		private StarGroup(int startPositionX, int startPositionY, int offsetX, int rating, int ratingCount) {
			for (int i = 0; i < ratingCount; i++) {
				TextureRegion checkedStarTexture = Assets.settingsSkin.getRegion("full_star");
				Drawable checkedStarDrawable = new TextureRegionDrawable(checkedStarTexture);
				TextureRegion emptyStarTexture = Assets.settingsSkin.getRegion("empty_star");
				Drawable emptyStarDrawable = new TextureRegionDrawable(emptyStarTexture);
				ImageButton starButton = new ImageButton(emptyStarDrawable, emptyStarDrawable, checkedStarDrawable);
				starButton.setBounds(startPositionX + (i * offsetX), startPositionY,
						checkedStarTexture.getRegionWidth(), checkedStarTexture.getRegionHeight());
				starButton.center();
				starButton.clearListeners();
				this.addActor(starButton);
			}
			for (int i = 0; i < rating; i++) {
				toggleRating();
			}
		}
		public void toggleRating() {
			updateRating();
			updateButtons();
		}
		private void updateRating() {
			if (rating == this.getChildren().size) {
				rating = 1;
			} else {
				rating++;
			}
		}
		private void updateButtons() {
			for (int i = 0; i < rating; i++) {
				ImageButton starButton = (ImageButton) this.getChildren().get(i);
				starButton.setChecked(true);
			}
			for (int j = rating; j < this.getChildren().size; j++) {
				ImageButton starButton = (ImageButton) this.getChildren().get(j);
				starButton.setChecked(false);
			}
		}
	}

	SinglePlayerSettingsScreen(Game game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();

		Persistence persistence = Persistence.getInstance();
		gameSettings = GameSettingsLoader.loadLevel(persistence.getCurrentLevel());

		addGridLines();

		addLevelTitle();
		addDifficultyGroup();

		addEvenOdd(GroupState.VISIBLE);
		addSingleDouble(GroupState.VISIBLE);
		addPrimeComposite(persistence);
		addMiddleEdge(persistence);

		addDiagonalsGroup(GroupState.VISIBLE);
		addFourSquaresGroup(persistence);

		if (persistence.isInPlay()) {
			addResumeButton();
		} else {
			addPlayButton();
			addBackButton();
		}
	}

	private void addGridLines() {
		int[] position = { 324, 524, 806 };
		GridLines gridLines = new GridLines(position);
		stage.addActor(gridLines);
	}

	private void addLevelTitle() {
		Label levelLabel = buildLevelLabel();
		stage.addActor(levelLabel);
	}
	private Label buildLevelLabel() {
		int level = gameSettings.getLevel();
		Label.LabelStyle gillSansLightStyle = new Label.LabelStyle();
		BitmapFont font = FontGenerator.getGillSansLight200();
		gillSansLightStyle.font = font;
		gillSansLightStyle.fontColor = Launch.BRIGHT_YELLOW;
		Label levelLabel = new Label("" + level, gillSansLightStyle);
		levelLabel.setAlignment(Align.center);
		levelLabel.setBounds(0, Launch.SCREEN_HEIGHT - 340, Launch.SCREEN_WIDTH, 300);
		return levelLabel;
	}

	private void addDifficultyGroup() {
		Label difficultyLabel = buildDifficultyLabel();
		int difficultyRating = gameSettings.getDifficulty();
		StarGroup starGroup = new StarGroup(158, Launch.SCREEN_HEIGHT - 490, difficultyRating);
		DifficultyGroup difficultyGroup = new DifficultyGroup(difficultyLabel, starGroup, GroupState.VISIBLE);
		stage.addActor(difficultyGroup);
	}
	private Label buildDifficultyLabel() {
		Label difficultyLabel = new Label("Difficulty", labelStyle57);
		difficultyLabel.setAlignment(Align.center);
		difficultyLabel.setPosition(220, Launch.SCREEN_HEIGHT - 416);
		return difficultyLabel;
	}

	private void addPrimeComposite(Persistence persistence) {
		if (persistence.getMaxLevel() > 3) {
			addPrimeComposite(GroupState.VISIBLE);
		} else {
			addPrimeComposite(GroupState.TRANSPARENT);
		}
	}

	private void addMiddleEdge(Persistence persistence) {
		if (persistence.getMaxLevel() > 6) {
			addMiddleEdge(GroupState.VISIBLE);
		} else {
			addMiddleEdge(GroupState.TRANSPARENT);
		}
	}

	private void addFourSquaresGroup(Persistence persistence) {
		if (persistence.getMaxLevel() > 13) {
			addFourSquaresGroup(GroupState.VISIBLE);
		} else {
			addFourSquaresGroup(GroupState.TRANSPARENT);
		}
	}

	private void addPlayButton() {
		ClickListener listener = new ClickListenerWithSound() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				GameManager manager = SinglePlayerGameManager.createNewGameManager();
				ScreenTracker.transitionScreen.transition(Direction.RIGHT, ScreenTracker.playScreen);
				manager.startNewGame();
			}
		};
		ForwardBottomNavigation forwardBottomNavigation = new ForwardBottomNavigation("Game", listener);
		stage.addActor(forwardBottomNavigation);
	}
	private void addBackButton() {
		BackBottomNavigation backBottomNavigation = new BackBottomNavigation("Levels", ScreenTracker.levelsScreen);
		stage.addActor(backBottomNavigation);
	}
	private void addResumeButton() {
		ClickListener listener = new ClickListenerWithSound() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenTracker.transitionScreen.transition(Direction.RIGHT, ScreenTracker.playScreen);
			}
		};
		ForwardBottomNavigation forwardBottomNavigation = new ForwardBottomNavigation("Game", listener);
		stage.addActor(forwardBottomNavigation);
	}

	@Override
	public void render(float delta) {
		handleBackKey();
		super.render(delta);
	}
	private void handleBackKey() {
		if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
			backKey = true;
		} else if (backKey) {
			backKey = false;
			Persistence persistence = Persistence.getInstance();
			if (persistence.isInPlay()) {
				ScreenTracker.transitionScreen.transition(Direction.RIGHT, ScreenTracker.playScreen);
			} else {
				ScreenTracker.transitionScreen.transition(Direction.LEFT, ScreenTracker.levelsScreen);
			}
		}
	}
	@Override
	public void pause() {
		Persistence persistence = Persistence.getInstance();
		persistence.setCurrentScreen(TAG);
		persistence.setMode(ModeTracker.mode.name());
		if (persistence.isInPlay()) {
			GameManager gameManager = SinglePlayerGameManager.getInstance();
			CortexState currentState = gameManager.getState();
			persistence.setCurrentCortexState(currentState);
		}
	}
}
