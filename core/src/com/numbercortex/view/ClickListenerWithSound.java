package com.numbercortex.view;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.numbercortex.Persistence;

public class ClickListenerWithSound extends ClickListener {
	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		super.touchDown(event, x, y, pointer, button);
		if (Persistence.getInstance().isSound()) {
			Sound.click();
		}
		return true;
	}
}
