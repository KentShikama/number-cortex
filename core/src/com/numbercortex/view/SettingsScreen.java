package com.numbercortex.view;

import libgdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.numbercortex.GameSettings;

abstract class SettingsScreen extends GameScreen {

	GameSettings gameSettings;

	static Label.LabelStyle labelStyle50 = buildLabelStyle50();
	private static Label.LabelStyle buildLabelStyle50() {
		Label.LabelStyle labelStyle50 = new Label.LabelStyle();
		BitmapFont gillSans50Compact = Assets.gillSans50Compact;
		labelStyle50.font = gillSans50Compact;
		labelStyle50.fontColor = Launch.BRIGHT_YELLOW;
		return labelStyle50;
	}

	static Label.LabelStyle labelStyle57 = buildLabelStyle57();
	static Label.LabelStyle buildLabelStyle57() {
		Label.LabelStyle labelStyle57 = new Label.LabelStyle();
		BitmapFont gillSans57 = Assets.gillSans57;
		labelStyle57.font = gillSans57;
		labelStyle57.fontColor = Launch.BRIGHT_YELLOW;
		return labelStyle57;
	}

	static TextField.TextFieldStyle textFieldStyle = buildTextFieldStyle();
	static TextField.TextFieldStyle buildTextFieldStyle() {
		TextureRegion textFieldTexture = Assets.settingsSkin.getRegion("name_texfield");
		Drawable textFieldDrawable = new TextureRegionDrawable(textFieldTexture);
		TextField.TextFieldStyle style = new TextField.TextFieldStyle();
		style.background = textFieldDrawable;
		style.font = Assets.gillSans57;
		style.fontColor = Launch.BRIGHT_YELLOW;
		style.messageFont = Assets.gillSans57;
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
			checkbox.addListener(new ClickListenerWithSound() {
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

	class GridLines extends Group {
		GridLines(int[] position) {
			TextureRegion gridLineTexture = Assets.settingsSkin.getRegion("grid_line");
			for (int i = 0; i < position.length; i++) {
				Image gridLine = new Image(gridLineTexture);
				gridLine.setPosition(23, Launch.SCREEN_HEIGHT - position[i]);
				this.addActor(gridLine);
			}
		}
	}

	SettingsScreen(Game game) {
		super(game);
	}

	@Override
	public void show() {
		FitViewport fitViewport = new FitViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		stage = new Stage(fitViewport);
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(true);
		backKey = false;
		reloadLabelStylesIfApplicable();
	}
	private void reloadLabelStylesIfApplicable() {
		if (labelStyle50 == null) {
			labelStyle50 = buildLabelStyle50();
		}
		if (labelStyle57 == null) {
			labelStyle57 = buildLabelStyle57();
		}
	}
	void addEvenOdd(GroupState state) {
		Label evenOddLabel = buildEvenOddLabel();
		ImageButton evenOddCheckbox = buildEvenOddCheckbox();
		CheckboxSettingGroup evenOddGroup = new CheckboxSettingGroup(evenOddLabel, evenOddCheckbox, state);
		stage.addActor(evenOddGroup);
	}
	private Label buildEvenOddLabel() {
		Label evenOddLabel = new Label("Even\nOdd", labelStyle50);
		evenOddLabel.setAlignment(Align.center);
		evenOddLabel.setPosition(65 - 6, Launch.SCREEN_HEIGHT - 665);
		return evenOddLabel;
	}
	private ImageButton buildEvenOddCheckbox() {
		int positionX = 206;
		int positionY = Launch.SCREEN_HEIGHT - 634;
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

	void addSingleDouble(GroupState state) {
		Label singleDoubleLabel = buildSingleDoubleLabel();
		ImageButton singleDoubleCheckbox = buildSingleDoubleCheckbox();
		CheckboxSettingGroup singleDoubleGroup = new CheckboxSettingGroup(singleDoubleLabel, singleDoubleCheckbox,
				state);
		stage.addActor(singleDoubleGroup);
	}
	private Label buildSingleDoubleLabel() {
		Label singleDoubleLabel = new Label("Single\nDouble", labelStyle50);
		singleDoubleLabel.setAlignment(Align.center);
		singleDoubleLabel.setPosition(349 - 6, Launch.SCREEN_HEIGHT - 665);
		return singleDoubleLabel;
	}
	private ImageButton buildSingleDoubleCheckbox() {
		int positionX = 528;
		int positionY = Launch.SCREEN_HEIGHT - 634;
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

	void addPrimeComposite(GroupState state) {
		Label primeCompositeLabel = buildPrimeCompositeLabel();
		ImageButton primeCompositeCheckbox = buildPrimeCompositeCheckbox();
		CheckboxSettingGroup primeCompositeGroup = new CheckboxSettingGroup(primeCompositeLabel,
				primeCompositeCheckbox, state);
		stage.addActor(primeCompositeGroup);
	}
	private Label buildPrimeCompositeLabel() {
		Label primeCompositeLabel = new Label("Prime\nComp.", labelStyle50);
		primeCompositeLabel.setAlignment(Align.center);
		primeCompositeLabel.setPosition(47 - 6, Launch.SCREEN_HEIGHT - 788);
		return primeCompositeLabel;
	}
	private ImageButton buildPrimeCompositeCheckbox() {
		int positionX = 206;
		int positionY = Launch.SCREEN_HEIGHT - 757;
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

	void addMiddleEdge(GroupState state) {
		Label middleEdgeLabel = buildMiddleEdgeLabel();
		ImageButton middleEdgeCheckbox = buildMiddleEdgeCheckbox();
		CheckboxSettingGroup middleEdgeGroup = new CheckboxSettingGroup(middleEdgeLabel, middleEdgeCheckbox, state);
		stage.addActor(middleEdgeGroup);
	}
	private Label buildMiddleEdgeLabel() {
		Label middleEdgeLabel = new Label("Middle\nEdge", labelStyle50);
		middleEdgeLabel.setAlignment(Align.center);
		middleEdgeLabel.setPosition(339 - 6, Launch.SCREEN_HEIGHT - 788);
		return middleEdgeLabel;
	}
	private ImageButton buildMiddleEdgeCheckbox() {
		int positionX = 528;
		int positionY = Launch.SCREEN_HEIGHT - 757;
		boolean isChecked = gameSettings.isMiddleEdge();
		final ImageButton middleEdgeCheckbox = buildCheckbox(positionX, positionY, isChecked);
		middleEdgeCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameSettings.setMiddleEdge(middleEdgeCheckbox.isChecked());
			}
		});
		return middleEdgeCheckbox;
	}

	void addDiagonalsGroup(GroupState state) {
		Label diagonalsLabel = buildDiagonalsLabel();
		ImageButton diagonalsCheckbox = buildDiagonalsCheckbox();
		Image diagonalsIcon = buildDiagonalsIcon();
		CheckboxSettingGroup diagonalsGroup = new CheckboxSettingGroup(diagonalsLabel, diagonalsCheckbox,
				diagonalsIcon, state);
		stage.addActor(diagonalsGroup);
	}
	private Label buildDiagonalsLabel() {
		Label diagonalsLabel = new Label("Diag.", labelStyle50);
		diagonalsLabel.setAlignment(Align.center);
		diagonalsLabel.setPosition(71, Launch.SCREEN_HEIGHT - 958);
		return diagonalsLabel;
	}
	private ImageButton buildDiagonalsCheckbox() {
		int positionX = 206;
		int positionY = Launch.SCREEN_HEIGHT - 930;
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
		int positionY = Launch.SCREEN_HEIGHT - 890;
		TextureRegion iconTexture = Assets.settingsSkin.getRegion("diagonals_icon");
		Image diagonalsIcon = buildIcon(iconTexture, positionX, positionY);
		return diagonalsIcon;
	}

	void addFourSquaresGroup(GroupState state) {
		Label fourSquareLabel = buildFourSquareLabel();
		ImageButton fourSquareCheckbox = buildFourSquareCheckbox();
		Image fourSquareIcon = buildFourSquareIcon();
		CheckboxSettingGroup fourSquareGroup = new CheckboxSettingGroup(fourSquareLabel, fourSquareCheckbox,
				fourSquareIcon, state);
		stage.addActor(fourSquareGroup);
	}
	private Label buildFourSquareLabel() {
		Label fourSquareLabel = new Label("Four\nSquare", labelStyle50);
		fourSquareLabel.setAlignment(Align.center);
		fourSquareLabel.setPosition(350, Launch.SCREEN_HEIGHT - 989);
		return fourSquareLabel;
	}
	private ImageButton buildFourSquareCheckbox() {
		int positionX = 528;
		int positionY = Launch.SCREEN_HEIGHT - 930;
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
		int positionY = Launch.SCREEN_HEIGHT - 868;
		TextureRegion iconTexture = Assets.settingsSkin.getRegion("four_square_icon");
		Image diagonalsIcon = buildIcon(iconTexture, positionX, positionY);
		return diagonalsIcon;
	}

	static Image buildIcon(TextureRegion iconTexture, int positionX, int positionY) {
		Image diagonalsIcon = new Image(iconTexture);
		diagonalsIcon.setPosition(positionX, positionY);
		return diagonalsIcon;
	}
	static ImageButton buildCheckbox(int positionX, int positionY, boolean isChecked) {
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
	public void render(float delta) {
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.act(delta);
		stage.draw();
	}
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	public static void disposeAll() {
		labelStyle50 = null;
		labelStyle57 = null;
		textFieldStyle = null;
	}
}
