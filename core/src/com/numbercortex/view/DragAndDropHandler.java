package com.numbercortex.view;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.numbercortex.Persistence;
import com.numbercortex.logic.InteractableSendable;

public class DragAndDropHandler {

	private DragAndDrop handler = new DragAndDrop();
	private NumberTextButton nextNumber;
	private InteractableSendable messenger;
	private int chosenNumber;

	private int placementCount;
	public static final int MAXIMUM_PLACEMENT_COUNT = 2;

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

	public void setSendable(InteractableSendable messenger) {
		this.messenger = messenger;
	}

	public void setChosenNumber(int chosenNumber) {
		this.chosenNumber = chosenNumber;
	}

	public int getPlacementCount() {
		return placementCount;
	}
	public void resetPlacementCount() {
		this.placementCount = 0;
	}

	class NumberSource extends Source {

		private NumberTextButton sourceButton;

		public NumberSource(NumberTextButton button) {
			super(button);
			this.sourceButton = button;
		}

		@Override
		public Payload dragStart(InputEvent event, float x, float y, int pointer) {
			if (isValidToDrag()) {
				Payload payload = new Payload();
				Label buttonLabel = sourceButton.getLabel();
				sourceButton.clearActions();
				payload.setObject(buttonLabel);
				payload.setDragActor(buttonLabel);
				payload.setInvalidDragActor(buttonLabel);
				payload.setValidDragActor(buttonLabel);
				handler.setDragActorPosition(-(buttonLabel.getWidth() / 2), buttonLabel.getHeight() / 2);
				return payload;
			}
			return null;
		}
		private boolean isValidToDrag() {
			return messenger != null && isChosenNumber(sourceButton) && Persistence.getInstance().isInPlay()
					&& isFirstPlacementForTutorialLevel(sourceButton) && isPlacementCountUnderMaximum();
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
		private boolean isFirstPlacementForTutorialLevel(NumberTextButton sourceButton) {
			if (Persistence.getInstance().getCurrentLevel() == 0
					&& !sourceButton.getName().equals(MessageArea.NEXT_NUMBER_SQUARE_NAME)) {
				return false;
			}
			return true;
		}
		private boolean isPlacementCountUnderMaximum() {
			if (placementCount <= MAXIMUM_PLACEMENT_COUNT) {
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
				placementCount++;
				Sound.click();
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
