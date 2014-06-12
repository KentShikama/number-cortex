package com.numberquarto;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
	private RatingGroup difficultyGroup;

	private static final int CHECKBOX_LENGTH = 84;
	private static final int STAR_WIDTH = 78;
	private static final int STAR_HEIGHT = 75;

	class RatingGroup {
		private ArrayList<Button> list = new ArrayList<Button>();
		private boolean isDisabled;
		private int rating;

		public boolean isDisabled() {
			return isDisabled;
		}

		public void setDisabled(boolean isDisabled) {
			this.isDisabled = isDisabled;
			for (Button button : list) {
				button.setDisabled(isDisabled);
			}
		}

		public void add(Button button) {
			list.add(button);
		}

		public int getRating() {
			return rating;
		}

		public void setRating(int rating) {
			this.rating = rating;
			for (int i = 0; i < rating; i++) {
				list.get(i).setChecked(true);
			}
			for (int j = rating; j < list.size(); j++) {
				list.get(j).setChecked(false);
			}
		}

		public int getButtonCount() {
			return list.size();
		}
	}

	class DifficultyGroupListener extends ClickListener {
		public void clicked(InputEvent event, float x, float y) {
			if (difficultyGroup.isDisabled())
				return;

			int buttonCount = difficultyGroup.getButtonCount();
			int checkedButtonCount = difficultyGroup.getRating();
			if (checkedButtonCount == buttonCount) {
				difficultyGroup.setRating(1);
			} else {
				difficultyGroup.setRating(checkedButtonCount + 1);
			}
		}
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
		easyButton.removeListener(easyButton.getClickListener());
		easyButton.addListener(new DifficultyGroupListener());
		mediumButton = new ImageButton(emptyStar, emptyStar, fullStar);
		mediumButton.setBounds(351 + 105, Launch.SCREEN_HEIGHT - 721,
				STAR_WIDTH, STAR_HEIGHT);
		mediumButton.removeListener(mediumButton.getClickListener());
		mediumButton.addListener(new DifficultyGroupListener());

		hardButton = new ImageButton(emptyStar, emptyStar, fullStar);
		hardButton.setBounds(351 + (105 * 2), Launch.SCREEN_HEIGHT - 721,
				STAR_WIDTH, STAR_HEIGHT);
		hardButton.removeListener(hardButton.getClickListener());
		hardButton.addListener(new DifficultyGroupListener());

		difficultyGroup = new RatingGroup();
		difficultyGroup.add(easyButton);
		difficultyGroup.add(mediumButton);
		difficultyGroup.add(hardButton);
		difficultyGroup.setRating(2);

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
