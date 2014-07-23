package com.numbercortex.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.numbercortex.CortexState;
import com.numbercortex.ModeTracker;
import com.numbercortex.Persistence;
import com.numbercortex.logic.GameManager;
import com.numbercortex.logic.TwoPlayerGameManager;

class TwoPlayerSettingsScreen extends SettingsScreen {

	public static final String TAG = "Two Player Settings Screen";

	private static TextField.TextFieldStyle textFieldStyle = buildTextFieldStyle();
	private static TextField.TextFieldStyle buildTextFieldStyle() {
		TextureRegion textFieldTexture = Assets.settingsSkin.getRegion("name_texfield");
		Drawable textFieldDrawable = new TextureRegionDrawable(textFieldTexture);
		TextField.TextFieldStyle style = new TextField.TextFieldStyle();
		style.background = textFieldDrawable;
		style.font = FontGenerator.getGillSans57();
		style.fontColor = Launch.BRIGHT_YELLOW;
		style.messageFont = FontGenerator.getGillSans57();
		style.messageFontColor = new Color(Launch.SEA_BLUE).add(0.2f, 0.2f, 0.2f, 0);
		style.background.setLeftWidth(style.background.getLeftWidth() + 15);
		style.background.setRightWidth(style.background.getRightWidth() + 15);
		addSelectionStyle(style);
		addCursorStyle(style);
		return style;
	}
	private static void addSelectionStyle(TextField.TextFieldStyle style) {
		Pixmap bluePixmap = new Pixmap(1, 1, Format.RGBA8888);
		bluePixmap.setColor(new Color(Launch.SEA_BLUE).sub(0.5f, 0.5f, 0.5f, 0.5f));
		bluePixmap.fill();
		Assets.settingsSkin.add("selection", new Texture(bluePixmap));
		Drawable selectionDrawable = Assets.settingsSkin.getDrawable("selection");
		style.selection = selectionDrawable;
	}
	private static void addCursorStyle(TextField.TextFieldStyle style) {
		Pixmap pixmap = new Pixmap(2, 70, Format.RGBA8888);
		pixmap.setColor(Launch.BRIGHT_YELLOW);
		pixmap.fill();
		Assets.settingsSkin.add("cursor", new Texture(pixmap));
		TextureRegion cursorTexture = Assets.settingsSkin.getRegion("cursor");
		Drawable cursorDrawable = new TextureRegionDrawable(cursorTexture);
		style.cursor = cursorDrawable;
		style.cursor.setMinWidth(2f);
	}

	private TextField playerOneNameField;
	private TextField playerTwoNameField;

	private GroupState groupState;

	class TwoChoiceRadioSettingGroup extends SettingGroup {
		public TwoChoiceRadioSettingGroup(Label choiceOneLabel, Label choiceTwoLabel, ImageButton choiceOneCheckbox,
				ImageButton choiceTwoCheckbox, final GroupState groupState) {
			super(groupState);
			this.addActor(choiceOneLabel);
			this.addActor(choiceTwoLabel);
			this.addActor(choiceOneCheckbox);
			this.addActor(choiceTwoCheckbox);
			ButtonGroup group = new ButtonGroup(choiceOneCheckbox, choiceTwoCheckbox);
			for (final Button button : group.getButtons()) {
				button.addListener(new ClickListenerWithSound() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if (groupState == GroupState.CLICKABLE) {
							button.setChecked(!button.isChecked());
						}
					}
				});
			}
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			SnapshotArray<Actor> children = this.getChildren();
			if (groupState == GroupState.TRANSPARENT) {
				parentAlpha = 0.5f;
			}
			for (Actor child : children) {
				child.draw(batch, parentAlpha);
			}
		}
	}

	class SpinnerSettingGroup extends SettingGroup {

		public SpinnerSettingGroup(Image icon, SpinnerGroup spinner, GroupState groupState) {
			super(groupState);
			this.addActor(icon);
			this.addActor(spinner);
			if (groupState != GroupState.CLICKABLE) {
				for (Actor actor : spinner.getChildren()) {
					actor.clearListeners();
				}
			}
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			SnapshotArray<Actor> children = this.getChildren();
			if (groupState == GroupState.TRANSPARENT) {
				parentAlpha = 0.5f;
			}
			for (Actor child : children) {
				child.draw(batch, parentAlpha);
			}
		}
	}
	class SpinnerGroup extends Group {
		private int value;
		public SpinnerGroup(int initialValue, final Label label, Image increaseValue, Image decreaseValue) {
			value = initialValue;
			label.setText(value + "s");
			this.addActor(label);
			this.addActor(increaseValue);
			this.addActor(decreaseValue);
			increaseValue.addListener(new ClickListenerWithSound() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (value >= 999) {
						return;
					}
					value++;
					label.setText(value + "s");
					gameSettings.setTime(value);
				}
			});
			decreaseValue.addListener(new ClickListenerWithSound() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (value <= 1) {
						return;
					}
					value--;
					label.setText(value + "s");
					gameSettings.setTime(value);
				}
			});
		}
	}

	class TextFieldSettingGroup extends SettingGroup {

		public TextFieldSettingGroup(Label label, TextField textField, GroupState groupState) {
			super(groupState);
			this.addActor(label);
			this.addActor(textField);
			if (groupState != GroupState.CLICKABLE) {
				textField.setDisabled(true);
			}
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			SnapshotArray<Actor> children = this.getChildren();
			if (groupState == GroupState.TRANSPARENT) {
				parentAlpha = 0.5f;
			}
			for (Actor child : children) {
				child.draw(batch, parentAlpha);
			}
		}
	}

	TwoPlayerSettingsScreen(Game game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();

		Persistence persistence = Persistence.getInstance();
		gameSettings = persistence.getTwoPlayerGameSettings();
		if (persistence.isInPlay()) {
			groupState = GroupState.VISIBLE;
		} else {
			groupState = GroupState.CLICKABLE;
		}

		addGridLines();

		addPlayerOneName(persistence);
		addPlayerTwoName(persistence);

		addTime();
		addBoardSize();

		addEvenOdd();
		addSingleDouble();

		addPrimeComposite();
		addMiddleExtreme();

		addDiagonalsGroup();
		addFourSquaresGroup();
	}

	private void addGridLines() {
		int[] position = { 276, 496, 778, 962 };
		GridLines gridLines = new GridLines(position);
		stage.addActor(gridLines);
	}

	private void addPlayerOneName(Persistence persistence) {
		Label playerOneNameLabel = buildPlayerOneNameLabel();
		playerOneNameField = buildPlayerOneNameField(persistence);
		TextFieldSettingGroup playerOneNameGroup = new TextFieldSettingGroup(playerOneNameLabel, playerOneNameField,
				groupState);
		stage.addActor(playerOneNameGroup);
	}
	private Label buildPlayerOneNameLabel() {
		Label label = new Label("Name (P1)", labelStyle57);
		label.setAlignment(Align.center);
		label.setPosition(48 - 6, Launch.SCREEN_HEIGHT - 111);
		return label;
	}
	private TextField buildPlayerOneNameField(Persistence persistence) {
		String playerOneName = persistence.getPlayerOneName();
		if (textFieldStyle == null) {
			textFieldStyle = buildTextFieldStyle();
		}
		TextField playerOneNameField = new TextField(playerOneName, textFieldStyle);
		playerOneNameField.setBounds(324, Launch.SCREEN_HEIGHT - 130, 268, 93);
		playerOneNameField.setMaxLength(20);
		playerOneNameField.setMessageText("Edit...");
		return playerOneNameField;
	}

	private void addPlayerTwoName(Persistence persistence) {
		Label playerTwoNameLabel = buildPlayerTwoNameLabel();
		playerTwoNameField = buildPlayerTwoNameField(persistence);
		TextFieldSettingGroup playerTwoNameGroup = new TextFieldSettingGroup(playerTwoNameLabel, playerTwoNameField,
				groupState);
		stage.addActor(playerTwoNameGroup);
	}
	private Label buildPlayerTwoNameLabel() {
		Label label = new Label("Name (P2)", labelStyle57);
		label.setAlignment(Align.center);
		label.setPosition(48 - 6, Launch.SCREEN_HEIGHT - 228);
		return label;
	}
	private TextField buildPlayerTwoNameField(Persistence persistence) {
		String playerTwoName = persistence.getPlayerTwoName();
		if (textFieldStyle == null) {
			textFieldStyle = buildTextFieldStyle();
		}
		TextField playerTwoNameField = new TextField(playerTwoName, textFieldStyle);
		playerTwoNameField.setBounds(324, Launch.SCREEN_HEIGHT - 247, 268, 93);
		playerTwoNameField.setMaxLength(20);
		playerTwoNameField.setMessageText("Edit...");
		return playerTwoNameField;
	}

	private void addTime() {
		Image icon = buildTimeIcon();
		Label label = buildTimeLabel();
		Image increaseValueControlImage = buildIncreaseValueControlImage();
		Image decreaseValueControlImage = buildDecreaseValueControlImage();
		int initialValue = gameSettings.getTime();
		SpinnerGroup spinnerGroup = new SpinnerGroup(initialValue, label, increaseValueControlImage,
				decreaseValueControlImage);
		SpinnerSettingGroup timeGroup = new SpinnerSettingGroup(icon, spinnerGroup, GroupState.TRANSPARENT);
		stage.addActor(timeGroup);
	}
	private Image buildTimeIcon() {
		int positionX = 90;
		int positionY = Launch.SCREEN_HEIGHT - 380;
		TextureRegion iconTexture = Assets.settingsSkin.getRegion("time_icon");
		Image timeIcon = buildIcon(iconTexture, positionX, positionY);
		return timeIcon;
	}
	private Label buildTimeLabel() {
		Label label = new Label("", labelStyle57);
		label.setAlignment(Align.center);
		label.setBounds(76, Launch.SCREEN_HEIGHT - 456, 100, 60);
		return label;
	}
	private Image buildIncreaseValueControlImage() {
		int positionX = 209;
		int positionY = Launch.SCREEN_HEIGHT - 376;
		TextureRegion iconTexture = Assets.settingsSkin.getRegion("up_arrow");
		Image increaseValueControlImage = buildIcon(iconTexture, positionX, positionY);
		return increaseValueControlImage;
	}
	private Image buildDecreaseValueControlImage() {
		int positionX = 209;
		int positionY = Launch.SCREEN_HEIGHT - 454;
		TextureRegion iconTexture = Assets.settingsSkin.getRegion("down_arrow");
		Image decreaseValueControlImage = buildIcon(iconTexture, positionX, positionY);
		return decreaseValueControlImage;
	}

	private void addBoardSize() {
		Label label3x3 = buildLabel3x3();
		Label label4x4 = buildLabel4x4();
		ImageButton checkbox3x3 = buildCheckbox3x3();
		ImageButton checkbox4x4 = buildCheckbox4x4();
		TwoChoiceRadioSettingGroup boardSizeGroup = new TwoChoiceRadioSettingGroup(label3x3, label4x4, checkbox3x3,
				checkbox4x4, groupState);
		stage.addActor(boardSizeGroup);
	}
	private Label buildLabel3x3() {
		Label label = new Label("3x3", labelStyle57);
		label.setAlignment(Align.center);
		label.setPosition(466 - 6, Launch.SCREEN_HEIGHT - 368);
		return label;
	}
	private Label buildLabel4x4() {
		Label label = new Label("4x4", labelStyle57);
		label.setAlignment(Align.center);
		label.setPosition(466 - 6, Launch.SCREEN_HEIGHT - 470);
		return label;
	}
	private ImageButton buildCheckbox3x3() {
		int positionX = 364;
		int positionY = Launch.SCREEN_HEIGHT - 366;
		boolean isChecked = (gameSettings.getNumberOfRows() == 3);
		final ImageButton checkbox3x3 = buildCheckbox(positionX, positionY, isChecked);
		checkbox3x3.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameSettings.setNumberOfRows(checkbox3x3.isChecked() ? 3 : 4);
			}
		});
		return checkbox3x3;
	}
	private ImageButton buildCheckbox4x4() {
		int positionX = 364;
		int positionY = Launch.SCREEN_HEIGHT - 469;
		boolean isChecked = (gameSettings.getNumberOfRows() == 4);
		final ImageButton checkbox4x4 = buildCheckbox(positionX, positionY, isChecked);
		checkbox4x4.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameSettings.setNumberOfRows(checkbox4x4.isChecked() ? 4 : 3);
			}
		});
		return checkbox4x4;
	}

	private void addEvenOdd() {
		Label evenOddLabel = buildEvenOddLabel();
		ImageButton evenOddCheckbox = buildEvenOddCheckbox();
		CheckboxSettingGroup evenOddGroup = new CheckboxSettingGroup(evenOddLabel, evenOddCheckbox, groupState);
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
				gameSettings.setEvenOdd(evenOddCheckbox.isChecked());
			}
		});
		return evenOddCheckbox;
	}

	private void addSingleDouble() {
		Label singleDoubleLabel = buildSingleDoubleLabel();
		ImageButton singleDoubleCheckbox = buildSingleDoubleCheckbox();
		CheckboxSettingGroup singleDoubleGroup = new CheckboxSettingGroup(singleDoubleLabel, singleDoubleCheckbox,
				groupState);
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
		CheckboxSettingGroup primeCompositeGroup = new CheckboxSettingGroup(primeCompositeLabel,
				primeCompositeCheckbox, groupState);
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
		CheckboxSettingGroup middleExtremeGroup = new CheckboxSettingGroup(middleExtremeLabel, middleExtremeCheckbox,
				groupState);
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
				diagonalsIcon, groupState);
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
		CheckboxSettingGroup fourSquareGroup = new CheckboxSettingGroup(fourSquareLabel, fourSquareCheckbox,
				fourSquareIcon, groupState);
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

	@Override
	void addPlayButton() {
		ClickListener listener = new ClickListenerWithSound() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				saveUnsyncedPreferences();
				GameManager manager = TwoPlayerGameManager.createNewGameManager();
				game.setScreen(ScreenTracker.playScreen);
				manager.startNewGame();
			}
		};
		ForwardBottomNavigation forwardBottomNavigation = new ForwardBottomNavigation("Game", listener);
		stage.addActor(forwardBottomNavigation);
	}
	@Override
	void addBackButton() {
		BackBottomNavigation backBottomNavigation = new BackBottomNavigation("Home", ScreenTracker.titleScreen);
		stage.addActor(backBottomNavigation);
	}
	@Override
	void addResumeButton() {
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
		if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
			backKey = true;
		} else if (backKey) {
			backKey = false;
			Persistence persistence = Persistence.getInstance();
			if (persistence.isInPlay()) {
				resumeAction();
			} else {
				game.setScreen(ScreenTracker.titleScreen);
			}
		}
		super.render(delta);
	}
	private void resumeAction() {
		GameManager manager = TwoPlayerGameManager.getInstance();
		game.setScreen(ScreenTracker.playScreen);
		manager.resumeGame();
	}
	@Override
	public void dispose() {
		textFieldStyle = null;
	}
	@Override
	public void hide() {
		saveUnsyncedPreferences();
	}
	@Override
	public void pause() {
		saveUnsyncedPreferences();
		saveOtherPreferences();
	}
	private void saveUnsyncedPreferences() {
		Persistence persistence = Persistence.getInstance();
		persistence.setPlayerOneName(playerOneNameField.getText());
		persistence.setPlayerTwoName(playerTwoNameField.getText());
	}
	private void saveOtherPreferences() {
		Persistence persistence = Persistence.getInstance();
		persistence.setCurrentScreen(TAG);
		persistence.setMode(ModeTracker.mode.name());
		if (persistence.isInPlay()) {
			GameManager gameManager = TwoPlayerGameManager.getInstance();
			CortexState currentState = gameManager.getState();
			persistence.setCurrentCortexState(currentState);
		}
	}
}
