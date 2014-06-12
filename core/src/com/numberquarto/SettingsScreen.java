package com.numberquarto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SettingsScreen implements Screen {

	private Stage stage;
	private Skin skin = Assets.settingsSkin;

	private ImageButton diagonalsCheckbox;
	private ImageButton fourSquareCheckbox;
	private ImageButton musicCheckbox;
	private ImageButton easyButton;
	private ImageButton mediumButton;
	private ImageButton hardButton;

	private static final int CHECKBOX_LENGTH = 84;
	private static final int STAR_WIDTH = 78;
	private static final int STAR_HEIGHT = 75;

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		if (diagonalsCheckbox != null && diagonalsCheckbox.isChecked()) {
			System.out.println("diagonals checkbox is checked");
		}
	}

	@Override
	public void show() {
		stage = new Stage(new FitViewport(Launch.SCREEN_WIDTH,
				Launch.SCREEN_HEIGHT));
		Gdx.input.setInputProcessor(stage);

		Drawable emptyCheckbox = skin.getDrawable("empty_checkbox");
		Drawable checkedCheckbox = skin.getDrawable("checked_checkbox");

		diagonalsCheckbox = new ImageButton(emptyCheckbox, emptyCheckbox,
				checkedCheckbox);
		diagonalsCheckbox.setBounds(416, Launch.SCREEN_HEIGHT - 488,
				CHECKBOX_LENGTH, CHECKBOX_LENGTH);
		diagonalsCheckbox.setChecked(true);
		diagonalsCheckbox.left();
		fourSquareCheckbox = new ImageButton(emptyCheckbox, emptyCheckbox,
				checkedCheckbox);
		fourSquareCheckbox.setBounds(416, Launch.SCREEN_HEIGHT - 593,
				CHECKBOX_LENGTH, CHECKBOX_LENGTH);
		fourSquareCheckbox.left();

		Drawable emptyStar = skin.getDrawable("empty_star");
		Drawable fullStar = skin.getDrawable("full_star");

		easyButton = new ImageButton(emptyStar, emptyStar, fullStar);
		easyButton.setBounds(351, Launch.SCREEN_HEIGHT - 721, STAR_WIDTH,
				STAR_HEIGHT);
		mediumButton = new ImageButton(emptyStar, emptyStar, fullStar);
		mediumButton.setBounds(351 + 105, Launch.SCREEN_HEIGHT - 721,
				STAR_WIDTH, STAR_HEIGHT);
		hardButton = new ImageButton(emptyStar, emptyStar, fullStar);
		hardButton.setBounds(351 + (105 * 2), Launch.SCREEN_HEIGHT - 721,
				STAR_WIDTH, STAR_HEIGHT);

		easyButton.setChecked(true);
		mediumButton.setChecked(true);

		ButtonGroup difficultyGroup = new ButtonGroup() {
			protected boolean canCheck(Button button, boolean newState) {
				if (button.isChecked() == newState)
					return false;

				Array<Button> buttons = this.getButtons();
				Array<Button> checkedButtons = this.getAllChecked();
				int buttonCount = buttons.size;
				int checkedButtonCount = checkedButtons.size;
				for (int i = 0, n = buttons.size; i < n; i++) {
					Button currentButton = buttons.get(i);
					currentButton.setChecked(false);
				}
				if (checkedButtonCount == buttonCount) {
					Button currentButton = buttons.get(0);
					currentButton.setChecked(true);
					checkedButtons.add(currentButton);
				} else {
					for (int i = 0; i <= checkedButtonCount; i++) {
						Button currentButton = buttons.get(i);
						currentButton.setChecked(true);
						checkedButtons.add(currentButton);
					}
				}
				return true;
			}
		};
		difficultyGroup.add(easyButton);
		difficultyGroup.add(mediumButton);
		difficultyGroup.add(hardButton);

		musicCheckbox = new ImageButton(emptyCheckbox, emptyCheckbox,
				checkedCheckbox);
		musicCheckbox.setBounds(416, Launch.SCREEN_HEIGHT - 844,
				CHECKBOX_LENGTH, CHECKBOX_LENGTH);
		musicCheckbox.setChecked(true);
		musicCheckbox.left();

		stage.addActor(diagonalsCheckbox);
		stage.addActor(fourSquareCheckbox);
		stage.addActor(easyButton);
		stage.addActor(mediumButton);
		stage.addActor(hardButton);
		stage.addActor(musicCheckbox);

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
