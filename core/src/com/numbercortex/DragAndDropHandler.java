package com.numbercortex;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;

/**
 * Differentiate next_number rectangle and board rectangles by name?
 * 
 * Allow for some processing to be done here to save bandwidth.
 * The results will still be doubled checked by the model.
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
	private NumberCortexBoard board;
	private MessageArea messageArea;
	private Local local;
	private NumberTextButton nextNumber;

	public void notifyBoardConstruction(NumberCortexBoard board) {
		this.board = board;
		ArrayList<NumberTextButton> cells = board.getBoardCells();
		for (NumberTextButton button : cells) {
			handler.addSource(new NumberSource(button));
			handler.addTarget(new NumberTarget(button));
		}
	}
	
	public void notifyMessageAreaConstrucion(MessageArea messageArea) {
		this.messageArea = messageArea;
		nextNumber = messageArea.getNextNumberSquare();
		handler.addSource(new NumberSource(nextNumber));
		handler.addTarget(new NumberTarget(nextNumber));
	}
	
	public void setExchangeable(Local local) {
		this.local = local;
	}
	
	private boolean isButtonEmpty(NumberTextButton button) {
		return button.getLabel().getText().toString().isEmpty();
	}
	
	private boolean isButtonStatic(NumberTextButton button) {
		int coordinate = Integer.valueOf(button.getName());
		
		// TODO
		
		return false;
	}

	class NumberSource extends Source {

		private NumberTextButton sourceButton;

		public NumberSource(NumberTextButton button) {
			super(button);
			this.sourceButton = button;
		}

		@Override
		public Payload dragStart(InputEvent event, float x, float y, int pointer) {
			if (isButtonEmpty(sourceButton) || isButtonStatic(sourceButton)) {
				return null;
			}
			Payload payload = new Payload();
			Label buttonLabel = sourceButton.getLabel();
			payload.setObject(buttonLabel);
			payload.setDragActor(buttonLabel);
			payload.setInvalidDragActor(buttonLabel);
			payload.setValidDragActor(buttonLabel);
			handler.setDragActorPosition(-(buttonLabel.getWidth() / 2),
					buttonLabel.getHeight() / 2);
			sourceButton.clearLabel();
			return payload;
			
		}

		@Override
		public void dragStop(InputEvent event, float x, float y, int pointer,
				Payload payload, Target target) {
			Label label = (Label) payload.getObject();
			if (validDrop(target)) {
				NumberTextButton targetButton = (NumberTextButton) target.getActor();
				targetButton.setLabel(label);
				int targetCoordinate = Integer.valueOf(targetButton.getName());
				int targetValue = Integer.valueOf(label.getText().toString());
				local.placeNumber(targetCoordinate, targetValue);
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
		public boolean drag(Source source, Payload payload, float x, float y,
				int pointer) {
			if (isButtonEmpty(targetButton)) {
				return true;
			} else {
				return false;
			}
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
