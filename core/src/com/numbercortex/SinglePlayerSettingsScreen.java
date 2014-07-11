package com.numbercortex;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

public class SinglePlayerSettingsScreen implements Screen {

	private static final String TAG = SinglePlayerSettingsScreen.class.getCanonicalName();

	private Stage stage;
	private Game game;
	private GameSettings gameSettings;
	
	enum GroupState {
		CLICKABLE, VISIBLE, TRANSPARENT;
	}
	
	class SettingGroup extends Actor {
		protected GroupState groupState;
		public SettingGroup(GroupState groupState) {
			this.groupState = groupState;
		}
	}
	
	class CheckBoxSettingGroup extends SettingGroup {
		private Image icon;
		private Label label;
		private ImageButton checkbox;
		
		CheckBoxSettingGroup(Image icon, Label label, final ImageButton checkbox, final GroupState groupState) {
			super(groupState);
			this.icon = icon;
			this.label = label;
			this.checkbox = checkbox;
			setBounds(checkbox.getX(), checkbox.getY(), checkbox.getWidth(), checkbox.getHeight());
			addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					if (groupState == GroupState.CLICKABLE) {						
						checkbox.setChecked(!checkbox.isChecked());
					}
				}
			});
		}
		
		public void draw(Batch batch, float parentAlpha) {
			if (groupState == GroupState.TRANSPARENT) {
				parentAlpha = 0.5f;
			}
			icon.draw(batch, parentAlpha);
			label.draw(batch, parentAlpha);
			checkbox.draw(batch, parentAlpha);
		}
	}

	public SinglePlayerSettingsScreen(Game game) {
		this.game = game;
		stage = ((Launch) game).getStage();
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
	
	private static Label.LabelStyle labelStyle50 = buildLabelStyle50();
	private static Label.LabelStyle buildLabelStyle50() {
		Label.LabelStyle labelStyle50 = new Label.LabelStyle();
		labelStyle50.font = FontGenerator.getGillSans50();
		labelStyle50.fontColor = Launch.BRIGHT_YELLOW;
		return labelStyle50;
	}

	@Override
	public void show() {
		stage.clear();
		gameSettings = GameSettingsLoader.loadLevel(ScreenTracker.level);
		
		BackgroundScreen background = new BackgroundScreen(Launch.SEA_BLUE, Assets.backgroundTexture);
		stage.addActor(background);
		
		addDiagonalsGroup();
		
		if (ScreenTracker.isInPlay) {
			buildResumeButton();
		} else {
			buildPlayButton();
			buildBackButton();
		}
	}

	private void addDiagonalsGroup() {
		Image diagonalsIcon = buildDiagonalsIcon();
		Label diagonalsLabel = buildDiagonalsLabel();
		ImageButton diagonalsCheckbox = buildDiagonalsCheckbox();
		CheckBoxSettingGroup diagonalsGroup = new CheckBoxSettingGroup(diagonalsIcon, diagonalsLabel, diagonalsCheckbox, GroupState.TRANSPARENT);
		stage.addActor(diagonalsGroup);
	}
	private Image buildDiagonalsIcon() {
		int positionX = 94;
		int positionY = Launch.SCREEN_HEIGHT - 862;
		TextureRegion iconTexture = Assets.settingsSkin.getRegion("diagonals_icon");
		Image diagonalsIcon = buildIcon(iconTexture, positionX, positionY);
		return diagonalsIcon;
	}
	private Label buildDiagonalsLabel() {
		Label diagonalsLabel = new Label("Diags.", labelStyle50);
		diagonalsLabel.setPosition(71, Launch.SCREEN_HEIGHT - 929);
		return diagonalsLabel;
	}
	private ImageButton buildDiagonalsCheckbox() {
		int positionX = 206;
		int positionY = Launch.SCREEN_HEIGHT - 898;
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
		final ImageButton checkbox = new ImageButton(emptyCheckBoxDrawable, emptyCheckBoxDrawable, checkedCheckboxDrawable);
		checkbox.setBounds(positionX, positionY, checkedCheckboxTexture.getRegionWidth(), checkedCheckboxTexture.getRegionHeight());
		checkbox.left();
		checkbox.bottom();
		checkbox.setChecked(isChecked);
		return checkbox;
	}
	
	private void buildPlayButton() {
		TextureRegion textButtonTexture = Assets.settingsSkin.getRegion("button_rectangle");
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = FontGenerator.getGillSans60();
		textButtonStyle.fontColor = Launch.BRIGHT_YELLOW;
		textButtonStyle.up = new TextureRegionDrawable(textButtonTexture);
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
	private void buildBackButton() {
		TextureRegion textButtonTexture = Assets.settingsSkin.getRegion("button_rectangle");
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = FontGenerator.getGillSans60();
		textButtonStyle.fontColor = Launch.BRIGHT_YELLOW;
		textButtonStyle.up = new TextureRegionDrawable(textButtonTexture);
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
	private void buildResumeButton() {
		TextureRegion textButtonTexture = Assets.settingsSkin.getRegion("button_rectangle");
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = FontGenerator.getGillSans60();
		textButtonStyle.fontColor = Launch.BRIGHT_YELLOW;
		textButtonStyle.up = new TextureRegionDrawable(textButtonTexture);
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
	public void hide() {
		CortexPreferences.getInstance().save();
	}

	@Override
	public void resume() {
		Assets.loadSettings();
	}

	@Override
	public void dispose() {}

	@Override
	public void pause() {}

}
