package com.numbercortex.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.numbercortex.CortexState;
import com.numbercortex.ModeTracker;
import com.numbercortex.Persistence;
import com.numbercortex.logic.GameManager;
import com.numbercortex.logic.SinglePlayerGameManager;
import com.numbercortex.logic.TwoPlayerGameManager;
import com.numbercortex.view.TransitionScreen.Direction;

public class OptionsScreen extends SettingsScreen {

	public static final String TAG = "Options Screen";
	private Table table = new Table();
	private Label textFieldFeedback;

	OptionsScreen(Game game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();

		Persistence persistence = Persistence.getInstance();

		table.clear();
		table.setBounds(0, 100, Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT - 100);
		addTitle();
		addYourName();
		addYourNameTextField(persistence);
		addGridLine();
		addSound(persistence);
		addMusic(persistence);
		addTextFieldFeedback();
		stage.addActor(table);

		addNavigationButton();
	}

	private void addTitle() {
		Label optionsTitle = buildTitle();

		TextureRegion optionsIconTexture = Assets.settingsSkin.getRegion("options_icon");
		Image optionsIcon = new Image(optionsIconTexture);

		Table titleTable = new Table();
		titleTable.add(optionsTitle).right().pad(14);
		titleTable.add(optionsIcon).left().pad(14).padTop(30);

		table.add(titleTable).expandX().padTop(10).padBottom(80).colspan(2);
		table.row();
	}
	private Label buildTitle() {
		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = FontGenerator.getGillSansLight140();
		labelStyle.fontColor = Launch.BRIGHT_YELLOW;
		Label title = new Label("Options", labelStyle);
		return title;
	}

	private void addYourName() {
		Label yourNameLabel = new Label("Your Name", labelStyle57);
		table.add(yourNameLabel).center().colspan(2).padBottom(24);
		table.row();
	}

	private void addYourNameTextField(Persistence persistence) {
		TextField yourNameField = buildYourNameTextField(persistence);
		table.add(yourNameField).center().width(268).colspan(2).padBottom(60);
		table.row();
	}
	private TextField buildYourNameTextField(Persistence persistence) {
		String yourName = persistence.getYourName();
		if (textFieldStyle == null) {
			textFieldStyle = buildTextFieldStyle();
		}
		TextField yourNameTextField = new TextField(yourName, textFieldStyle);
		TextField.TextFieldListener listener = buildYourNameFieldListener();
		yourNameTextField.setTextFieldListener(listener);
		yourNameTextField.setMaxLength(20);
		if (persistence.isInPlay()) {
			yourNameTextField.setDisabled(true);
			yourNameTextField.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Sound.missClick();
					textFieldFeedback.setText("Cannot edit during game");
				}
			});
		}
		return yourNameTextField;
	}
	private TextField.TextFieldListener buildYourNameFieldListener() {
		TextField.TextFieldListener listener = new TextField.TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				String yourName = textField.getText();
				Persistence persistence = Persistence.getInstance();
				persistence.setYourName(yourName);
			}
		};
		return listener;
	}

	private void addGridLine() {
		TextureRegion gridLineTexture = Assets.settingsSkin.getRegion("grid_line");
		Image gridLine = new Image(gridLineTexture);
		table.add(gridLine).center().colspan(2).padBottom(40);
		table.row();
	}

	private void addSound(Persistence persistence) {
		Label soundLabel = new Label("Sound", labelStyle57);
		table.add(soundLabel).right().padRight(48).padBottom(24);
		ImageButton soundCheckbox = buildSoundCheckbox(persistence);
		table.add(soundCheckbox).width(78).left().bottom().padBottom(24);
		table.row();
	}
	private ImageButton buildSoundCheckbox(final Persistence persistence) {
		boolean isChecked = persistence.isSound();
		final ImageButton soundCheckbox = buildCheckbox(0, 0, isChecked);
		soundCheckbox.addListener(new ClickListenerWithSound() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				soundCheckbox.setChecked(!soundCheckbox.isChecked());
			}
		});
		soundCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				persistence.setSound(soundCheckbox.isChecked());
			}
		});
		return soundCheckbox;
	}

	private void addMusic(Persistence persistence) {
		Label musicLabel = new Label("Music", labelStyle57);
		table.add(musicLabel).right().padRight(48).padBottom(64);
		ImageButton musicCheckbox = buildMusicCheckbox(persistence);
		table.add(musicCheckbox).width(78).left().bottom().padBottom(64);
		table.row();
	}
	private ImageButton buildMusicCheckbox(final Persistence persistence) {
		boolean isChecked = persistence.isMusic();
		final ImageButton musicCheckbox = buildCheckbox(0, 0, isChecked);
		musicCheckbox.addListener(new ClickListenerWithSound() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				musicCheckbox.setChecked(!musicCheckbox.isChecked());
			}
		});
		musicCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				persistence.setMusic(musicCheckbox.isChecked());
			}
		});
		return musicCheckbox;
	}

	private void addTextFieldFeedback() {
		textFieldFeedback = new Label(" ", labelStyle57);
		table.add(textFieldFeedback).center().colspan(2);
		table.row();
	}

	private void addNavigationButton() {
		Persistence persistence = Persistence.getInstance();
		String text;
		ClickListener listener;
		if (persistence.isInPlay()) {
			text = "Game";
			listener = new ClickListenerWithSound() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					resumeAction();
				}
			};
		} else {
			text = "Home";
			listener = new ClickListenerWithSound() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					ScreenTracker.transitionScreen.transition(Direction.LEFT, ScreenTracker.titleScreen);
				}
			};
		}
		BackBottomNavigation backBottomNavigation = new BackBottomNavigation(text, listener);
		stage.addActor(backBottomNavigation);
	}

	@Override
	public void render(float delta) {
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		handleBackKey();
		stage.act(delta);
		stage.draw();
	}
	private void handleBackKey() {
		if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
			backKey = true;
		} else if (backKey) {
			backKey = false;
			handleScreenSwitch();
		}
	}
	private void handleScreenSwitch() {
		Persistence persistence = Persistence.getInstance();
		if (persistence.isInPlay()) {
			resumeAction();
		} else {
			ScreenTracker.transitionScreen.transition(Direction.LEFT, ScreenTracker.titleScreen);
		}
	}
	private void resumeAction() {
		GameManager manager = getGameManagerInstance();
		ScreenTracker.transitionScreen.transition(Direction.LEFT, ScreenTracker.playScreen);
		manager.resumeGame();
	}
	@Override
	public void pause() {
		Persistence persistence = Persistence.getInstance();
		persistence.setCurrentScreen(TAG);
		if (persistence.isInPlay()) {
			persistence.setMode(ModeTracker.mode.name());
			GameManager gameManager = getGameManagerInstance();
			CortexState currentState = gameManager.getState();
			persistence.setCurrentCortexState(currentState);
		}
	}
	private GameManager getGameManagerInstance() {
		GameManager gameManager;
		if (ModeTracker.mode == ModeTracker.Mode.SINGLE_PLAYER) {
			gameManager = SinglePlayerGameManager.getInstance();
		} else {
			gameManager = TwoPlayerGameManager.getInstance();
		}
		return gameManager;
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
}
