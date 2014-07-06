package com.numbercortex;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class CortexDialog extends Dialog {
	
	private static Label.LabelStyle labelStyle = buildLabelStyle();
	private static Label.LabelStyle buildLabelStyle() {
		BitmapFont font = FontGenerator.getMessageFont();
		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = font;
		labelStyle.fontColor = Color.WHITE;
		return labelStyle;
	}
	
	private static TextButton.TextButtonStyle textButtonStyle = buildTextButtonStyle();
	private static TextButton.TextButtonStyle buildTextButtonStyle() {
		BitmapFont font = FontGenerator.getMessageFont();
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.fontColor = Color.WHITE;
		textButtonStyle.up = Assets.dialogSkin.getDrawable("white_button");
		return textButtonStyle;
	}

	
	private CortexDialog(String title, WindowStyle windowStyle) {
		super(title, windowStyle);
	}

	public static Dialog createCortexDialog() {
		Window.WindowStyle windowStyle = buildWindowStyle();
		
		Dialog dialog = new CortexDialog("", windowStyle) {
			protected void result (Object object) {
				System.out.println(object.toString());
			}
		};
		
		addContentLabel("Do you wish to continue?", dialog);
		
		addButton("Cancel", dialog);
		addButton("Confirm", dialog);
		
		return dialog;
	}
	private static void addContentLabel(String labelText, Dialog dialog) {
		Table contentTable = dialog.getContentTable();
		Label continueLabel = new Label(labelText, labelStyle);
		continueLabel.setWrap(true);
		continueLabel.setAlignment(Align.center);
		contentTable.add(continueLabel).width(540);
	}
	private static void addButton(String buttonText, Dialog dialog) {
		Table buttonTable = dialog.getButtonTable();
		TextButton cancelButton = new TextButton(buttonText, textButtonStyle);
		buttonTable.add(cancelButton).padBottom(60);
		dialog.setObject(cancelButton, buttonText);
	}


	private static Window.WindowStyle buildWindowStyle() {
		Window.WindowStyle windowStyle = new Window.WindowStyle();
		windowStyle.background = Assets.dialogSkin.getDrawable("pop_up");
		windowStyle.titleFont = FontGenerator.getMessageFont();
		return windowStyle;
	}
}
