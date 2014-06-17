package com.numbercortex;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;

/**
 * Differentiate next_number rectangle and board rectangles by name
 */
public class DragAndDropHandler {
	private static class Singleton {
		private static final DragAndDropHandler INSTANCE = new DragAndDropHandler();
	}

	private DragAndDropHandler() {
	}

	public static DragAndDropHandler getInstance() {
		return Singleton.INSTANCE;
	}

	private DragAndDrop handler = new DragAndDrop();
	private NumberQuartoBoard board;

	public void notifyBoardConstruction(NumberQuartoBoard board) {
		this.board = board;
		ArrayList<NumberTextButton> cells = board.getBoardCells();
		for (NumberTextButton button : cells) {
			handler.addSource(new NumberSource(button));
			handler.addTarget(new NumberTarget(button));
		}
	}

	class NumberSource extends Source {

		private NumberTextButton sourceButton;

		public NumberSource(NumberTextButton button) {
			super(button);
			this.sourceButton = button;
		}

		@Override
		public Payload dragStart(InputEvent event, float x, float y, int pointer) {
			if (sourceButton.getLabel() == null
					|| sourceButton.getLabel().getText().toString().isEmpty()) {
				return null;
			}
			Payload payload = new Payload();
			Label buttonLabel = sourceButton.getLabel();
			payload.setObject(buttonLabel);
			payload.setDragActor(buttonLabel);
			payload.setInvalidDragActor(buttonLabel);
			payload.setValidDragActor(buttonLabel);
			handler.setDragActorPosition(-(buttonLabel.getWidth()/2), buttonLabel.getHeight()/2);
			return payload;
		}

		@Override
		public void dragStop(InputEvent event, float x, float y,
				int pointer, Payload payload, Target target) {
			Label label = (Label) payload.getObject();
			if (target != null && !droppedOnSameSpot(target)) {
				NumberTextButton targetButton = (NumberTextButton) target.getActor();
				targetButton.setLabel(label);
			} else {
				sourceButton.setLabel(label);
			}
		}

		private boolean droppedOnSameSpot(Target target) {
			return target.getActor() == sourceButton;
		}

	}

	class NumberTarget extends Target {

		private NumberTextButton targetButton;

		public NumberTarget(NumberTextButton button) {
			super(button);
			this.targetButton = button;
		}

		@Override
		public boolean drag(Source source, Payload payload, float x, float y,
				int pointer) {
			return true;
		}

		@Override
		public void drop(Source source, Payload payload, float x, float y,
				int pointer) {

		}
		
		public NumberTextButton getButton() {
			return targetButton;
		}

	}

}
