package com.numbercortex;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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

	@Override
	protected void result(Object object) {}

	public static Dialog createConfirmationDialog(String dialogMessage) {
		Window.WindowStyle windowStyle = buildWindowStyle();
		CortexDialog dialog = new CortexDialog("", windowStyle);
		addContentLabel(dialogMessage, dialog);
		addButton("OK", null, dialog);
		return dialog;
	}

	public static Dialog createQuitCancelDialog(ClickListener quitListener) {
		Window.WindowStyle windowStyle = buildWindowStyle();
		CortexDialog dialog = new CortexDialog("", windowStyle);
		addContentLabel("Are you sure you want to quit? The current game data will be lost.", dialog);
		addButton("Quit", quitListener, dialog);
		addButton("Cancel", null, dialog);
		return dialog;
	}

	private static Window.WindowStyle buildWindowStyle() {
		Window.WindowStyle windowStyle = new Window.WindowStyle();
		windowStyle.background = Assets.dialogSkin.getDrawable("pop_up");
		windowStyle.titleFont = FontGenerator.getMessageFont();
		return windowStyle;
	}
	private static void addContentLabel(String labelText, Dialog dialog) {
		Table contentTable = dialog.getContentTable();
		Label contentLabel = new Label(labelText, labelStyle);
		contentLabel.setWrap(true);
		contentLabel.setAlignment(Align.center);
		contentTable.add(contentLabel).width(495).padRight(14);
	}
	private static void addButton(String buttonText, ClickListener listener, Dialog dialog) {
		Table buttonTable = dialog.getButtonTable();
		if (textButtonStyle == null) {
			textButtonStyle = buildTextButtonStyle();
		}
		TextButton button = new TextButton(buttonText, textButtonStyle);
		if (listener != null) {
			button.addListener(listener);
		}
		buttonTable.add(button).pad(4).padRight(14).padBottom(55);
		dialog.setObject(button, buttonText);
	}

	public static void dispose() {
		textButtonStyle = null;
	}
}
