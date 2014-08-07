package com.numbercortex.view;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import facebook.CrossPlatformFacebook;
import facebook.FacebookCallbackListener;

public class PlayScreenDialogDelegate {

    private boolean facebookShareDialogIsShowing;
    private Dialog facebookShareDialog;

    private boolean confirmationDialogIsShowing;
    private Dialog confirmationDialog;

    private CrossPlatformFacebook facebook;

    public PlayScreenDialogDelegate(CrossPlatformFacebook facebook) {
        this.facebook = facebook;
    }

    void draw(Stage stage) {
        if (facebookShareDialogIsShowing && facebookShareDialog.getStage() == null) {
            facebookShareDialog.show(stage);
        }
        if (!facebookShareDialogIsShowing && confirmationDialogIsShowing && confirmationDialog.getStage() == null) {
            confirmationDialog.show(stage);
        }
    }

    public void generateConfirmationDialog(Stage stage, float delay, final String dialogMessages) {
        DelayAction delayAction = Actions.delay(delay);
        Action showConfirmationDialogAction = buildShowConfirmationDialogAction(dialogMessages);
        stage.addAction(Actions.sequence(delayAction, showConfirmationDialogAction));
    }
    private Action buildShowConfirmationDialogAction(final String dialogMessages) {
        Action showConfirmationDialogAction = new Action() {
            @Override
            public boolean act(float delta) {
                confirmationDialogIsShowing = true;
                ClickListenerWithSound okayListener = buildConfirmationOKListener();
                confirmationDialog = CortexDialog.createConfirmationDialog(dialogMessages, okayListener);
                return true;
            }
        };
        return showConfirmationDialogAction;
    }
    private ClickListenerWithSound buildConfirmationOKListener() {
        ClickListenerWithSound okListener = new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                confirmationDialogIsShowing = false;
            }
        };
        return okListener;
    }

    public void generateShareDialogWithDelayIfApplicable(Stage stage, float delay, final String dialogMessage, final String facebookPostTitle, final String facebookPostDescription) {
        if (facebook == null) {
            System.out.println("Facebook sharing is not supported on this device");
        } else {
            DelayAction delayAction = Actions.delay(delay);
            Action showShareDialogAction = new Action() {
                @Override
                public boolean act(float delta) {
                    generateFacebookShareDialog(dialogMessage, facebookPostTitle, facebookPostDescription);
                    return true;
                }
            };
            stage.addAction(Actions.sequence(delayAction, showShareDialogAction));
        }
    }
    private void generateFacebookShareDialog(String dialogMessage, final String facebookPostTitle, final String facebookPostDescription) {
        ClickListenerWithSound cancelListener = buildFacebookCancelListener();
        ClickListenerWithSound shareListener = buildFacebookShareListener(facebookPostTitle, facebookPostDescription);
        facebookShareDialogIsShowing = true;
        facebookShareDialog = CortexDialog.createShareDialog(dialogMessage, cancelListener, shareListener);
    }
    private ClickListenerWithSound buildFacebookCancelListener() {
        ClickListenerWithSound cancelListener = new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                facebookShareDialogIsShowing = false;
            }
        };
        return cancelListener;
    }
    private ClickListenerWithSound buildFacebookShareListener(final String facebookPostTitle, final String facebookPostDescription) {
        ClickListenerWithSound shareListener = new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                facebookShareDialogIsShowing = false;
                FacebookCallbackListener listener = buildFacebookCallbackListener(facebookPostTitle, facebookPostDescription);
                facebook.setListener(listener);
                facebook.post(facebookPostTitle, facebookPostDescription);
            }
        };
        return shareListener;
    }
    private FacebookCallbackListener buildFacebookCallbackListener(final String facebookPostTitle, final String facebookPostDescription) {
        FacebookCallbackListener listener = new FacebookCallbackListener() {
            @Override
            public void showErrorDialog(String errorMessage) {
                generateFacebookShareDialog(errorMessage, facebookPostTitle, facebookPostDescription);
            }
        };
        return listener;
    }
}
