package com.numbercortex;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

public class SinglePlayerSettingsScreen implements Screen {

	private static final String TAG = SinglePlayerSettingsScreen.class.getCanonicalName();

	private Stage stage;
	private Game game;
	private GameSettings gameSettings;

	private static TextButton.TextButtonStyle textButtonStyle = buildTextButtonStyle();
	private static TextButton.TextButtonStyle buildTextButtonStyle() {
		TextureRegion textButtonTexture = Assets.settingsSkin.getRegion("button_rectangle");
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = FontGenerator.getGillSans57();
		textButtonStyle.fontColor = Launch.BRIGHT_YELLOW;
		textButtonStyle.up = new TextureRegionDrawable(textButtonTexture);
		return textButtonStyle;
	}

	private static Label.LabelStyle labelStyle50 = buildLabelStyle50();
	private static Label.LabelStyle buildLabelStyle50() {
		Label.LabelStyle labelStyle50 = new Label.LabelStyle();
		BitmapFont gillSans50Compact = FontGenerator.getGillSans50Compact();
		labelStyle50.font = gillSans50Compact;
		labelStyle50.fontColor = Launch.BRIGHT_YELLOW;
		return labelStyle50;
	}

	private static Label.LabelStyle labelStyle57 = buildLabelStyle57();
	private static Label.LabelStyle buildLabelStyle57() {
		Label.LabelStyle labelStyle57 = new Label.LabelStyle();
		BitmapFont gillSans57 = FontGenerator.getGillSans57();
		labelStyle57.font = gillSans57;
		labelStyle57.fontColor = Launch.BRIGHT_YELLOW;
		return labelStyle57;
	}

	enum GroupState {
		CLICKABLE, VISIBLE, TRANSPARENT;
	}

	class SettingGroup extends Group {
		protected GroupState groupState;
		public SettingGroup(GroupState groupState) {
			this.groupState = groupState;
		}
	}

	class CheckboxSettingGroup extends SettingGroup {
		CheckboxSettingGroup(Label label, ImageButton checkbox, GroupState groupState) {
			this(label, checkbox, null, groupState);
		}
		CheckboxSettingGroup(Label label, final ImageButton checkbox, Image icon, final GroupState groupState) {
			super(groupState);
			this.addActor(label);
			this.addActor(checkbox);
			if (icon != null) {
				this.addActor(icon);
			}
			checkbox.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (groupState == GroupState.CLICKABLE) {
						checkbox.setChecked(!checkbox.isChecked());
					}
				}
			});
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

	class DifficultyGroup extends SettingGroup {

		public DifficultyGroup(Label label, final StarGroup starGroup, final GroupState groupState) {
			super(groupState);
			SnapshotArray<Actor> starButtons = starGroup.getChildren();
			for (Actor starButton : starButtons) {
				starButton.addListener(new ClickListener() {
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

	public SinglePlayerSettingsScreen(Game game) {
		this.game = game;
		stage = ((Launch) game).getStage();
	}

	@Override
	public void show() {
		stage.clear();
		gameSettings = GameSettingsLoader.loadLevel(ScreenTracker.level);

		BackgroundScreen background = new BackgroundScreen(Launch.SEA_BLUE, Assets.backgroundTexture);
		stage.addActor(background);

		addDifficultyGroup();

		addEvenOdd();
		addSingleDouble();

		addPrimeComposite();
		addMiddleExtreme();

		addDiagonalsGroup();
		addFourSquaresGroup();

		if (ScreenTracker.isInPlay) {
			addResumeButton();
		} else {
			addPlayButton();
			addBackButton();
		}
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

	private void addPrimeComposite() {
		Label primeCompositeLabel = buildPrimeCompositeLabel();
		ImageButton primeCompositeCheckbox = buildPrimeCompositeCheckbox();
		GroupState state;
		if (CortexPreferences.getInstance().getCurrentLevel() > 3) {
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

	private void addMiddleExtreme() {
		Label middleExtremeLabel = buildMiddleExtremeLabel();
		ImageButton middleExtremeCheckbox = buildMiddleExtremeCheckbox();
		GroupState state;
		if (CortexPreferences.getInstance().getCurrentLevel() > 6) {
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

	private void addFourSquaresGroup() {
		Label fourSquareLabel = buildFourSquareLabel();
		ImageButton fourSquareCheckbox = buildFourSquareCheckbox();
		Image fourSquareIcon = buildFourSquareIcon();
		GroupState state;
		if (CortexPreferences.getInstance().getCurrentLevel() > 13) {
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
		if (textButtonStyle == null) {
			textButtonStyle = buildTextButtonStyle();
		}
		final TextButton playButton = new TextButton("Play", textButtonStyle);
		playButton.setBounds(306, Launch.SCREEN_HEIGHT - 1096, 284, 94);
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(ScreenTracker.playScreen);
			}
		});
		stage.addActor(playButton);
	}
	private void addBackButton() {
		if (textButtonStyle == null) {
			textButtonStyle = buildTextButtonStyle();
		}
		final TextButton backButton = new TextButton("Back", textButtonStyle);
		backButton.setBounds(55, Launch.SCREEN_HEIGHT - 1096, 222, 94);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(ScreenTracker.levelsScreen);
			}
		});
		stage.addActor(backButton);
	}
	private void addResumeButton() {
		if (textButtonStyle == null) {
			textButtonStyle = buildTextButtonStyle();
		}
		final TextButton resumeButton = new TextButton("Resume", textButtonStyle);
		resumeButton.setBounds(178, Launch.SCREEN_HEIGHT - 1096, 284, 94);
		resumeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(ScreenTracker.playScreen);
			}
		});
		stage.addActor(resumeButton);
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
	public void hide() {
		CortexPreferences.getInstance().save();
	}

	@Override
	public void resume() {
		if (FontGenerator.isNull()) {
			FontGenerator.load();
		}
	}

	@Override
	public void dispose() {
		textButtonStyle = null;
	}
	@Override
	public void pause() {}

}
