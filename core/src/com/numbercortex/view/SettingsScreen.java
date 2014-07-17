package com.numbercortex.view;

import com.badlogic.gdx.Game;
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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.SnapshotArray;
import com.numbercortex.GameSettings;
import com.numbercortex.Launch;

public class SettingsScreen {

	Stage stage;
	Game game;
	GameSettings gameSettings;

	static Label.LabelStyle labelStyle50 = buildLabelStyle50();
	private static Label.LabelStyle buildLabelStyle50() {
		Label.LabelStyle labelStyle50 = new Label.LabelStyle();
		BitmapFont gillSans50Compact = FontGenerator.getGillSans50Compact();
		labelStyle50.font = gillSans50Compact;
		labelStyle50.fontColor = Launch.BRIGHT_YELLOW;
		return labelStyle50;
	}

	static Label.LabelStyle labelStyle57 = buildLabelStyle57();
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

	class GridLines extends Group {
		GridLines(int[] position) {
			TextureRegion gridLineTexture = Assets.settingsSkin.getRegion("grid_line");
			for (int i = 0; i < position.length; i++) {
				Image gridLine = new Image(gridLineTexture);
				gridLine.setPosition(0, Launch.SCREEN_HEIGHT - position[i]);
				this.addActor(gridLine);
			}
		}
	}

	public SettingsScreen(Game game) {
		this.game = game;
		stage = ((Launch) game).getStage();
	}

	public void show() {
		stage.clear();
		BackgroundScreen background = new BackgroundScreen(Launch.SEA_BLUE, Assets.backgroundTexture);
		stage.addActor(background);
	}

}
