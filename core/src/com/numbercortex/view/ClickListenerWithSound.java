package com.numbercortex.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.numbercortex.Persistence;
import com.numbercortex.view.SettingsScreen.GroupState;
import com.numbercortex.view.SettingsScreen.SettingGroup;

public class ClickListenerWithSound extends ClickListener {
	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		super.touchDown(event, x, y, pointer, button);
		Actor targetActor = event.getTarget();
		SettingGroup settingGroup = getSettingsGroup(targetActor);
		if (settingGroup != null) {
			GroupState state = settingGroup.groupState;
			if (Persistence.getInstance().isSound() && state == GroupState.CLICKABLE) {
				Sound.click();
			}
		} else {
			if (Persistence.getInstance().isSound()) {
				Sound.click();
			}
		}
		return true;
	}
	
	private SettingGroup getSettingsGroup(Actor actor) {
		if (actor == null) {
			return null;
		} else if (actor instanceof SettingGroup) {
			return (SettingGroup) actor;
		} else {
			return getSettingsGroup(actor.getParent());
		}
	}
}
