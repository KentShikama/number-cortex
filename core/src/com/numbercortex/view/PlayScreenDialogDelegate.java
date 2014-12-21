package com.numbercortex.view;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

import facebook.CrossPlatformFacebook;
import facebook.FacebookCallbackListener;

class PlayScreenDialogDelegate {

    private boolean facebookShareDialogIsShowing;
    private Dialog facebookShareDialog;

    private boolean confirmationDialogIsShowing;
    private Dialog confirmationDialog;
    
    private boolean rateDialogIsShowing;
    private Dialog rateDialog;

    private boolean inMiddleOfAction;

    private CrossPlatformFacebook facebook;
	private String appLink;

    PlayScreenDialogDelegate(String appLink, CrossPlatformFacebook facebook) {
    	this.appLink = appLink;
        this.facebook = facebook;
    }

    void draw(Stage stage) {
        if (!inMiddleOfAction) {
            if (facebookShareDialogIsShowing && facebookShareDialog.getStage() == null) {
                facebookShareDialog.show(stage);
            }
            if (!facebookShareDialogIsShowing && confirmationDialogIsShowing && confirmationDialog.getStage() == null) {
                confirmationDialog.show(stage);
            }
            if (!facebookShareDialogIsShowing && !confirmationDialogIsShowing && rateDialogIsShowing && rateDialog.getStage() == null) {
            	rateDialog.show(stage);
            }
        }
    }

    void generateConfirmationDialogs(Stage stage, final String... dialogMessages) {
        confirmationDialogIsShowing = true;
        ClickListenerWithSound okayListener = buildConfirmationOKListener();
        confirmationDialog = createConfirmationDialogs(okayListener, dialogMessages);
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
    private Dialog createConfirmationDialogs(final ClickListenerWithSound okayListener, String... dialogMessages) {
        if (dialogMessages.length == 1) {
            return CortexDialog.createConfirmationDialog(dialogMessages[0], okayListener);
        } else {
            final String[] remainingDialogMessages = Arrays.copyOfRange(dialogMessages, 1, dialogMessages.length);
            return CortexDialog.createConfirmationDialog(dialogMessages[0], new ClickListenerWithSound() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    confirmationDialog = createConfirmationDialogs(okayListener, remainingDialogMessages);
                }
            });
        }
    }

	void generateRateDialogIfApplicable() {
		if (appLink == null) {
            System.out.println("Rating is not supported on this device");			
		} else {
			generateRateDialog(appLink);
		}
	}
    private void generateRateDialog(String appLink) {
        ClickListenerWithSound cancelListener = buildRateCancelListener();
        ClickListenerWithSound rateListener = buildRateRateListener(appLink);
        rateDialogIsShowing = false;
        rateDialog = CortexDialog.createRateDialog(cancelListener, rateListener);
	}
    private ClickListenerWithSound buildRateCancelListener() {
        ClickListenerWithSound cancelListener = new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	rateDialogIsShowing = false;
            }
        };
        return cancelListener;
    }
    private ClickListenerWithSound buildRateRateListener(final String appLink) {
        ClickListenerWithSound rateListener = new ClickListenerWithSound() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	rateDialogIsShowing = false;
                Gdx.net.openURI(appLink);
            }
        };
        return rateListener;
    }
    
	void generateShareDialogIfApplicable(final String dialogMessage, final String facebookPostTitle, final String facebookPostDescription) {
        if (facebook == null) {
            System.out.println("Facebook sharing is not supported on this device");
        } else {
            generateFacebookShareDialog(dialogMessage, facebookPostTitle, facebookPostDescription);
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

    void setInMiddleOfAction(boolean inMiddleOfAction) {
        this.inMiddleOfAction = inMiddleOfAction;
    }
}
