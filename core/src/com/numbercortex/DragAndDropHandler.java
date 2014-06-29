package com.numbercortex;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;

public class DragAndDropHandler {

	private DragAndDrop handler = new DragAndDrop();
	private NumberTextButton nextNumber;
	private Sendable messenger;
	private int chosenNumber;

	private DragAndDropHandler() {}
	private static class Singleton {
		private static final DragAndDropHandler INSTANCE = new DragAndDropHandler();
	}
	public static DragAndDropHandler getInstance() {
		return Singleton.INSTANCE;
	}

	public void notifyBoardConstruction(NumberCortexBoard board) {
		ArrayList<NumberTextButton> cells = board.getBoardCells();
		for (NumberTextButton button : cells) {
			handler.addSource(new NumberSource(button));
			handler.addTarget(new NumberTarget(button));
		}
	}
	public void notifyMessageAreaConstrucion(MessageArea messageArea) {
		nextNumber = messageArea.getNextNumberSquare();
		handler.addSource(new NumberSource(nextNumber));
		handler.addTarget(new NumberTarget(nextNumber));
	}

	public void setSendable(Sendable messenger) {
		this.messenger = messenger;
	}

	public void setChosenNumber(int chosenNumber) {
		this.chosenNumber = chosenNumber;
	}

	class NumberSource extends Source {

		private NumberTextButton sourceButton;

		public NumberSource(NumberTextButton button) {
			super(button);
			this.sourceButton = button;
		}

		@Override
		public Payload dragStart(InputEvent event, float x, float y, int pointer) {
			if (isChosenNumber(sourceButton)) {
				Payload payload = new Payload();
				Label buttonLabel = sourceButton.getLabel();
				payload.setObject(buttonLabel);
				payload.setDragActor(buttonLabel);
				payload.setInvalidDragActor(buttonLabel);
				payload.setValidDragActor(buttonLabel);
				handler.setDragActorPosition(-(buttonLabel.getWidth() / 2), buttonLabel.getHeight() / 2);
				return payload;
			}
			return null;
		}
		private boolean isChosenNumber(NumberTextButton button) {
			Label label = button.getLabel();
			String labelText = label.getText().toString();
			if (labelText.isEmpty()) {
				return false;
			}
			int labelValue = Integer.valueOf(labelText);
			if (labelValue == chosenNumber) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target) {
			Label label = (Label) payload.getObject();
			if (validDrop(target)) {
				NumberTextButton targetButton = (NumberTextButton) target.getActor();
				sourceButton.clearLabel();
				targetButton.setLabel(label);
				int targetCoordinate = Integer.valueOf(targetButton.getName());
				messenger.placeNumber(null, targetCoordinate);
			} else {
				sourceButton.setLabel(label);
			}
		}
		private boolean validDrop(Target target) {
			return target != null && !droppedOnSameSpot(target);
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
		public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
			if (isButtonEmpty(targetButton)) {
				return true;
			} else {
				return false;
			}
		}
		private boolean isButtonEmpty(NumberTextButton button) {
			return button.getLabel().getText().toString().isEmpty();
		}

		@Override
		public void drop(Source source, Payload payload, float x, float y, int pointer) {}

		public NumberTextButton getButton() {
			return targetButton;
		}
	}

}
