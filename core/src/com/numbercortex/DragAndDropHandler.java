package com.numbercortex;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
		ArrayList<TextButton> cells = board.getBoardCells();
		for (TextButton button : cells) {
			handler.addSource(new NumberSource(button));
			handler.addTarget(new NumberTarget(button));
		}
	}

	class NumberSource extends Source {

		private TextButton button;

		public NumberSource(TextButton button) {
			super(button);
			this.button = button;
		}

		@Override
		public Payload dragStart(InputEvent event, float x, float y, int pointer) {
			if (button.getLabel() == null
					|| button.getLabel().getText().toString().isEmpty()) {
				return null;
			}
			Payload payload = new Payload();
			Label buttonLabel = button.getLabel();
			payload.setObject(buttonLabel);
			payload.setDragActor(buttonLabel);
			payload.setInvalidDragActor(buttonLabel);
			payload.setValidDragActor(buttonLabel);
			return payload;
		}

		@Override
		public void dragStop(InputEvent event, float x, float y,
				int pointer, Payload payload, Target target) {
			if (target != null && target instanceof NumberTarget) {
				System.out.println("Number target was found");
				if (droppedOnSameSpot(target)) {
					System.out.println("Source button was cleared");
					button.setText("");
				}
			} else {
				System.out.println("Number target not found");
				button.add(payload.getDragActor());
			}
		}

		private boolean droppedOnSameSpot(Target target) {
			return target.getActor() != button;
		}

	}

	class NumberTarget extends Target {

		private TextButton button;

		public NumberTarget(TextButton button) {
			super(button);
			this.button = button;
		}

		@Override
		public boolean drag(Source source, Payload payload, float x, float y,
				int pointer) {
			return false;
		}

		@Override
		public void drop(Source source, Payload payload, float x, float y,
				int pointer) {
			Label buttonLabel = (Label) payload.getObject();
			button.setText(buttonLabel.getText().toString());
		}
		
		public TextButton getButton() {
			return button;
		}

	}

}
