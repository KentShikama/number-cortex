package com.numbercortex.view;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;

public class AnimationUtilities {
	private AnimationUtilities() {}

	static void delayFadeAndRemoveActor(Actor actor, float delay) {
		DelayAction delayAction = Actions.delay(delay);
		AlphaAction fadeAction = Actions.fadeOut(1f);
		fadeAction.setInterpolation(Interpolation.exp10Out);
		Action removeActor = new Action() {
			@Override
			public boolean act(float delta) {
				actor.remove();
				return true;
			}
		};
		actor.addAction(Actions.sequence(delayAction, fadeAction, removeActor));
	}
}
