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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
import com.numbercortex.logic.TwoPlayerGameManager;
import com.numbercortex.view.GameScreen.BackBottomNavigation;
import com.numbercortex.view.GameScreen.ForwardBottomNavigation;

class SinglePlayerSettingsScreen extends SettingsScreen {

	public static final String TAG = "Single Player Settings Screen";

	private static final String TEXT_BUTTON_BORDER_TEXTURE_NAME = "button_rectangle";
	private static TextButton.TextButtonStyle textButtonStyle = buildTextButtonStyle(TEXT_BUTTON_BORDER_TEXTURE_NAME);
	private static TextButton.TextButtonStyle buildTextButtonStyle(String textureButtonTextureName) {
		TextureRegion textButtonTexture = Assets.settingsSkin.getRegion(textureButtonTextureName);
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = FontGenerator.getGillSans57();
		textButtonStyle.fontColor = Launch.BRIGHT_YELLOW;
		textButtonStyle.up = new TextureRegionDrawable(textButtonTexture);
		return textButtonStyle;
	}

	class DifficultyGroup extends SettingGroup {

		public DifficultyGroup(Label label, final StarGroup starGroup, final GroupState groupState) {
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
	class StarGroup extends Group {
		private int rating;

		public StarGroup(int startPositionX, int startPositionY, int rating) {
			this(startPositionX, startPositionY, 68, rating, 5);
		}
		public StarGroup(int startPositionX, int startPositionY, int offsetX, int rating, int ratingCount) {
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

	class LabelSettingGroup extends SettingGroup {

		public LabelSettingGroup(Image icon, Label label, GroupState groupState) {
			super(groupState);
			this.addActor(icon);
			this.addActor(label);
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

	SinglePlayerSettingsScreen(Game game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();

		Persistence persistence = Persistence.getInstance();
		gameSettings = GameSettingsLoader.loadLevel(persistence.getCurrentLevel());

		addGridLines();

		addTitle();
		addLevelWrap();

		addTime();
		addBoardSizeGroup();

		addDifficultyGroup();

		addEvenOdd();
		addSingleDouble();

		addPrimeComposite(persistence);
		addMiddleExtreme(persistence);

		addDiagonalsGroup();
		addFourSquaresGroup(persistence);

		if (persistence.isInPlay()) {
			addResumeButton();
		} else {
			addPlayButton();
			addBackButton();
		}
	}

	private void addGridLines() {
		int[] position = { 230, 366, 496, 778, 962 };
		GridLines gridLines = new GridLines(position);
		stage.addActor(gridLines);
	}

	private void addTitle() {
		TextureRegion titleTexture = Assets.settingsSkin.getRegion("level_info_label");
		Image title = new Image(titleTexture);
		title.setPosition(64, Launch.SCREEN_HEIGHT - 152);
		stage.addActor(title);
	}

	private void addLevelWrap() {
		Image levelWrap = buildLevelWrap();
		Label levelLabel = buildLevelLabel();
		LabelSettingGroup timeGroup = new LabelSettingGroup(levelWrap, levelLabel, GroupState.VISIBLE);
		stage.addActor(timeGroup);
	}
	private Image buildLevelWrap() {
		int positionX = 507 - 20;
		int positionY = Launch.SCREEN_HEIGHT - 123 - 62;
		TextureRegion iconTexture = Assets.settingsSkin.getRegion("level_wrap");
		Image timeIcon = buildIcon(iconTexture, positionX, positionY);
		return timeIcon;
	}
	private Label buildLevelLabel() {
		int level = gameSettings.getLevel();
		Label.LabelStyle tahoma86Style = new Label.LabelStyle();
		BitmapFont tahoma86 = FontGenerator.getNumberScrollerFont();
		tahoma86Style.font = tahoma86;
		tahoma86Style.fontColor = Launch.BRIGHT_YELLOW;
		Label levelLabel = new Label("" + level, tahoma86Style);
		levelLabel.setAlignment(Align.center);
		levelLabel.setBounds(535 - 6 - 20, Launch.SCREEN_HEIGHT - 84 - 6 - 62, 94, 64);
		return levelLabel;
	}

	private void addTime() {
		Image timeIcon = buildTimeIcon();
		Label timeLabel = buildTimeLabel();
		LabelSettingGroup timeGroup = new LabelSettingGroup(timeIcon, timeLabel, GroupState.VISIBLE);
		stage.addActor(timeGroup);
	}
	private Image buildTimeIcon() {
		int positionX = 78;
		int positionY = Launch.SCREEN_HEIGHT - 332;
		TextureRegion iconTexture = Assets.settingsSkin.getRegion("time_icon");
		Image timeIcon = buildIcon(iconTexture, positionX, positionY);
		return timeIcon;
	}
	private Label buildTimeLabel() {
		int time = gameSettings.getTime();
		Label boardSizeLabel = new Label("N/A", labelStyle57); // TODO: Implement time in levels
		boardSizeLabel.setAlignment(Align.center);
		boardSizeLabel.setPosition(168 - 6, Launch.SCREEN_HEIGHT - 330 - 6);
		return boardSizeLabel;
	}

	private void addBoardSizeGroup() {
		Image boardSizeIcon = buildBoardSizeIcon();
		Label boardSizeLabel = buildBoardSizeLabel();
		LabelSettingGroup boardSizeGroup = new LabelSettingGroup(boardSizeIcon, boardSizeLabel, GroupState.VISIBLE);
		stage.addActor(boardSizeGroup);
	}
	private Image buildBoardSizeIcon() {
		int positionX = 341;
		int positionY = Launch.SCREEN_HEIGHT - 336;
		TextureRegion iconTexture = Assets.settingsSkin.getRegion("board_size_icon");
		Image boardSizeIcon = buildIcon(iconTexture, positionX, positionY);
		return boardSizeIcon;
	}
	private Label buildBoardSizeLabel() {
		int numberOfRows = gameSettings.getNumberOfRows();
		Label boardSizeLabel = new Label(numberOfRows + "x" + numberOfRows, labelStyle57);
		boardSizeLabel.setAlignment(Align.center);
		boardSizeLabel.setPosition(458 - 6, Launch.SCREEN_HEIGHT - 330 - 6);
		return boardSizeLabel;
	}

	private void addDifficultyGroup() {
		Label difficultyLabel = buildDifficultyLabel();
		int difficultyRating = gameSettings.getDifficulty();
		StarGroup starGroup = new StarGroup(276, Launch.SCREEN_HEIGHT - 456, difficultyRating);
		DifficultyGroup difficultyGroup = new DifficultyGroup(difficultyLabel, starGroup, GroupState.VISIBLE);
		stage.addActor(difficultyGroup);
	}
	private Label buildDifficultyLabel() {
		Label difficultyLabel = new Label("Difficulty", labelStyle57);
		difficultyLabel.setAlignment(Align.center);
		difficultyLabel.setPosition(41 - 6, Launch.SCREEN_HEIGHT - 447 - 12);
		return difficultyLabel;
	}

	private void addEvenOdd() {
		Label evenOddLabel = buildEvenOddLabel();
		ImageButton evenOddCheckbox = buildEvenOddCheckbox();
		CheckboxSettingGroup evenOddGroup = new CheckboxSettingGroup(evenOddLabel, evenOddCheckbox, GroupState.VISIBLE);
		stage.addActor(evenOddGroup);
	}
	private Label buildEvenOddLabel() {
		Label evenOddLabel = new Label("Even\nOdd", labelStyle50);
		evenOddLabel.setAlignment(Align.center);
		evenOddLabel.setPosition(65 - 6, Launch.SCREEN_HEIGHT - 621 - 12);
		return evenOddLabel;
	}
	private ImageButton buildEvenOddCheckbox() {
		int positionX = 206;
		int positionY = Launch.SCREEN_HEIGHT - 606;
		boolean isChecked = gameSettings.isEvenOdd();
		final ImageButton evenOddCheckbox = buildCheckbox(positionX, positionY, isChecked);
		evenOddCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("Changed");
				gameSettings.setEvenOdd(evenOddCheckbox.isChecked());
			}
		});
		return evenOddCheckbox;
	}

	private void addSingleDouble() {
		Label singleDoubleLabel = buildSingleDoubleLabel();
		ImageButton singleDoubleCheckbox = buildSingleDoubleCheckbox();
		CheckboxSettingGroup singleDoubleGroup = new CheckboxSettingGroup(singleDoubleLabel, singleDoubleCheckbox,
				GroupState.VISIBLE);
		stage.addActor(singleDoubleGroup);
	}
	private Label buildSingleDoubleLabel() {
		Label singleDoubleLabel = new Label("Single\nDouble", labelStyle50);
		singleDoubleLabel.setAlignment(Align.center);
		singleDoubleLabel.setPosition(349 - 6, Launch.SCREEN_HEIGHT - 621 - 12);
		return singleDoubleLabel;
	}
	private ImageButton buildSingleDoubleCheckbox() {
		int positionX = 528;
		int positionY = Launch.SCREEN_HEIGHT - 606;
		boolean isChecked = gameSettings.isSingleDouble();
		final ImageButton singleDoubleCheckbox = buildCheckbox(positionX, positionY, isChecked);
		singleDoubleCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameSettings.setSingleDouble(singleDoubleCheckbox.isChecked());
			}
		});
		return singleDoubleCheckbox;
	}

	private void addPrimeComposite(Persistence persistence) {
		Label primeCompositeLabel = buildPrimeCompositeLabel();
		ImageButton primeCompositeCheckbox = buildPrimeCompositeCheckbox();
		GroupState state;
		if (persistence.getMaxLevel() > 3) {
			state = GroupState.VISIBLE;
		} else {
			state = GroupState.TRANSPARENT;
		}
		CheckboxSettingGroup primeCompositeGroup = new CheckboxSettingGroup(primeCompositeLabel,
				primeCompositeCheckbox, state);
		stage.addActor(primeCompositeGroup);
	}
	private Label buildPrimeCompositeLabel() {
		Label primeCompositeLabel = new Label("Prime\nComp.", labelStyle50);
		primeCompositeLabel.setAlignment(Align.center);
		primeCompositeLabel.setPosition(47 - 6, Launch.SCREEN_HEIGHT - 744 - 12);
		return primeCompositeLabel;
	}
	private ImageButton buildPrimeCompositeCheckbox() {
		int positionX = 206;
		int positionY = Launch.SCREEN_HEIGHT - 729;
		boolean isChecked = gameSettings.isPrimeComposite();
		final ImageButton primeCompositeCheckbox = buildCheckbox(positionX, positionY, isChecked);
		primeCompositeCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameSettings.setPrimeComposite(primeCompositeCheckbox.isChecked());
			}
		});
		return primeCompositeCheckbox;
	}

	private void addMiddleExtreme(Persistence persistence) {
		Label middleExtremeLabel = buildMiddleExtremeLabel();
		ImageButton middleExtremeCheckbox = buildMiddleExtremeCheckbox();
		GroupState state;
		if (persistence.getMaxLevel() > 6) {
			state = GroupState.VISIBLE;
		} else {
			state = GroupState.TRANSPARENT;
		}
		CheckboxSettingGroup middleExtremeGroup = new CheckboxSettingGroup(middleExtremeLabel, middleExtremeCheckbox,
				state);
		stage.addActor(middleExtremeGroup);
	}
	private Label buildMiddleExtremeLabel() {
		Label middleExtremeLabel = new Label("Middle\nExtreme", labelStyle50);
		middleExtremeLabel.setAlignment(Align.center);
		middleExtremeLabel.setPosition(339 - 6, Launch.SCREEN_HEIGHT - 744 - 12);
		return middleExtremeLabel;
	}
	private ImageButton buildMiddleExtremeCheckbox() {
		int positionX = 528;
		int positionY = Launch.SCREEN_HEIGHT - 729;
		boolean isChecked = gameSettings.isMiddleExtreme();
		final ImageButton middleExtremeCheckbox = buildCheckbox(positionX, positionY, isChecked);
		middleExtremeCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameSettings.setMiddleExtreme(middleExtremeCheckbox.isChecked());
			}
		});
		return middleExtremeCheckbox;
	}

	private void addDiagonalsGroup() {
		Label diagonalsLabel = buildDiagonalsLabel();
		ImageButton diagonalsCheckbox = buildDiagonalsCheckbox();
		Image diagonalsIcon = buildDiagonalsIcon();
		CheckboxSettingGroup diagonalsGroup = new CheckboxSettingGroup(diagonalsLabel, diagonalsCheckbox,
				diagonalsIcon, GroupState.VISIBLE);
		stage.addActor(diagonalsGroup);
	}
	private Label buildDiagonalsLabel() {
		Label diagonalsLabel = new Label("Diag.", labelStyle50);
		diagonalsLabel.setAlignment(Align.center);
		diagonalsLabel.setPosition(71, Launch.SCREEN_HEIGHT - 929 - 6);
		return diagonalsLabel;
	}
	private ImageButton buildDiagonalsCheckbox() {
		int positionX = 206;
		int positionY = Launch.SCREEN_HEIGHT - 902;
		boolean isChecked = gameSettings.isDiagonals();
		final ImageButton diagonalsCheckbox = buildCheckbox(positionX, positionY, isChecked);
		diagonalsCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameSettings.setDiagonals(diagonalsCheckbox.isChecked());
			}
		});
		return diagonalsCheckbox;
	}
	private Image buildDiagonalsIcon() {
		int positionX = 94;
		int positionY = Launch.SCREEN_HEIGHT - 862;
		TextureRegion iconTexture = Assets.settingsSkin.getRegion("diagonals_icon");
		Image diagonalsIcon = buildIcon(iconTexture, positionX, positionY);
		return diagonalsIcon;
	}

	private void addFourSquaresGroup(Persistence persistence) {
		Label fourSquareLabel = buildFourSquareLabel();
		ImageButton fourSquareCheckbox = buildFourSquareCheckbox();
		Image fourSquareIcon = buildFourSquareIcon();
		GroupState state;
		if (persistence.getMaxLevel() > 13) {
			state = GroupState.VISIBLE;
		} else {
			state = GroupState.TRANSPARENT;
		}
		CheckboxSettingGroup fourSquareGroup = new CheckboxSettingGroup(fourSquareLabel, fourSquareCheckbox,
				fourSquareIcon, state);
		stage.addActor(fourSquareGroup);
	}
	private Label buildFourSquareLabel() {
		Label fourSquareLabel = new Label("Four\nSquare", labelStyle50);
		fourSquareLabel.setAlignment(Align.center);
		fourSquareLabel.setPosition(350, Launch.SCREEN_HEIGHT - 961);
		return fourSquareLabel;
	}
	private ImageButton buildFourSquareCheckbox() {
		int positionX = 528;
		int positionY = Launch.SCREEN_HEIGHT - 902;
		boolean isChecked = gameSettings.isFourSquare();
		final ImageButton fourSquareCheckbox = buildCheckbox(positionX, positionY, isChecked);
		fourSquareCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameSettings.setFourSquare(fourSquareCheckbox.isChecked());
			}
		});
		return fourSquareCheckbox;
	}
	private Image buildFourSquareIcon() {
		int positionX = 398;
		int positionY = Launch.SCREEN_HEIGHT - 840;
		TextureRegion iconTexture = Assets.settingsSkin.getRegion("four_square_icon");
		Image diagonalsIcon = buildIcon(iconTexture, positionX, positionY);
		return diagonalsIcon;
	}

	private static Image buildIcon(TextureRegion iconTexture, int positionX, int positionY) {
		Image diagonalsIcon = new Image(iconTexture);
		diagonalsIcon.setPosition(positionX, positionY);
		return diagonalsIcon;
	}
	private static ImageButton buildCheckbox(int positionX, int positionY, boolean isChecked) {
		TextureRegion checkedCheckboxTexture = Assets.settingsSkin.getRegion("checked_checkbox");
		Drawable checkedCheckboxDrawable = new TextureRegionDrawable(checkedCheckboxTexture);
		TextureRegion emptyCheckBoxTexture = Assets.settingsSkin.getRegion("empty_checkbox");
		Drawable emptyCheckBoxDrawable = new TextureRegionDrawable(emptyCheckBoxTexture);
		final ImageButton checkbox = new ImageButton(emptyCheckBoxDrawable, emptyCheckBoxDrawable,
				checkedCheckboxDrawable);
		checkbox.setBounds(positionX, positionY, checkedCheckboxTexture.getRegionWidth(),
				checkedCheckboxTexture.getRegionHeight());
		checkbox.left();
		checkbox.bottom();
		checkbox.setChecked(isChecked);
		checkbox.clearListeners();
		return checkbox;
	}

	private void addPlayButton() {
		ClickListener listener = new ClickListenerWithSound() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				GameManager manager = SinglePlayerGameManager.createNewGameManager();
				game.setScreen(ScreenTracker.playScreen);
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
				resumeAction();
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
				resumeAction();
			} else {
				game.setScreen(ScreenTracker.levelsScreen);
			}
		}
	}
	private void resumeAction() {
		GameManager manager = SinglePlayerGameManager.getInstance();
		game.setScreen(ScreenTracker.playScreen);
		manager.resumeGame();
	}
	@Override
	public void dispose() {
		super.dispose();
		textButtonStyle = null;
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
