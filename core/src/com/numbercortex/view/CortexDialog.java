package com.numbercortex.view;

import java.util.Arrays;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.numbercortex.Persistence;
import com.numbercortex.view.TransitionScreen.Direction;

class CortexDialog extends Dialog {

    private static Label.LabelStyle labelStyle = buildLabelStyle();
    private static Label.LabelStyle buildLabelStyle() {
        BitmapFont font = Assets.gillSans50;
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        return labelStyle;
    }

    private static TextButton.TextButtonStyle textButtonStyle = buildTextButtonStyle();
    private static TextButton.TextButtonStyle buildTextButtonStyle() {
        BitmapFont font = Assets.gillSans57;
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

    static Dialog createConfirmationDialogs(final Stage stage, String... dialogMessages) {
        if (dialogMessages.length == 1) {
            return createConfirmationDialog(dialogMessages[0]).show(stage);
        } else {
            final String[] remainingDialogMessages = Arrays.copyOfRange(dialogMessages, 1, dialogMessages.length);
            return createConfirmationDialog(dialogMessages[0], new ClickListenerWithSound() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    createConfirmationDialogs(stage, remainingDialogMessages);
                }
            }).show(stage);
        }
    }
    static Dialog createConfirmationDialog(String dialogMessage) {
        return createConfirmationDialog(dialogMessage, null);
    }
    static Dialog createConfirmationDialog(String dialogMessage, ClickListenerWithSound onConfirmListener) {
        Window.WindowStyle windowStyle = buildWindowStyle();
        CortexDialog dialog = new CortexDialog("", windowStyle);
        addContentLabel(dialogMessage, dialog);
        addButton("OK", onConfirmListener, dialog);
        return dialog;
    }

    static Dialog createShareDialog(String dialogMessage, ClickListenerWithSound cancelListener, ClickListenerWithSound shareListener) {
        Window.WindowStyle windowStyle = buildWindowStyle();
        CortexDialog dialog = new CortexDialog("", windowStyle);
        addContentLabel(dialogMessage, dialog);
        addButton("Cancel", cancelListener, dialog);
        addButton("Share", shareListener, dialog);
        return dialog;
    }

    static Dialog createGameQuitCancelDialog() {
        Dialog dialog = CortexDialog.createQuitCancelDialog(new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Persistence persistence = Persistence.getInstance();
                persistence.setInPlay(false);
                Sound.stopGameBGM();
                Sound.loopOpeningBGMGradually();
                ScreenTracker.transitionScreen.transition(Direction.LEFT, ScreenTracker.titleScreen);
            }
        });
        return dialog;
    }
    private static Dialog createQuitCancelDialog(ClickListenerWithSound quitListener) {
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
        windowStyle.titleFont = Assets.gillSans57;
        return windowStyle;
    }
    private static void addContentLabel(String labelText, Dialog dialog) {
        Table contentTable = dialog.getContentTable();
        if (labelStyle == null) {
            labelStyle = buildLabelStyle();
        }
        Label contentLabel = new Label(labelText, labelStyle);
        contentLabel.setWrap(true);
        contentLabel.setAlignment(Align.center);
        contentTable.add(contentLabel).width(495).padRight(14).padTop(26);
    }
    private static void addButton(String buttonText, ClickListenerWithSound listener, Dialog dialog) {
        Table buttonTable = dialog.getButtonTable();
        if (textButtonStyle == null) {
            textButtonStyle = buildTextButtonStyle();
        }
        TextButton button = new TextButton(buttonText, textButtonStyle);
        if (listener != null) {
            button.addListener(listener);
        } else {
            button.addListener(new ClickListenerWithSound());
        }
        buttonTable.add(button).height(94).padRight(14).padBottom(42).padTop(20);
        dialog.setObject(button, buttonText);
    }

    static void dispose() {
        labelStyle = null;
        textButtonStyle = null;
    }
}
