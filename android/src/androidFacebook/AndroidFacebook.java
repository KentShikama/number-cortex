package androidFacebook;

import android.os.Bundle;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.FacebookDialog.Callback;
import com.numbercortex.android.AndroidLauncher;
import facebook.CrossPlatformFacebook;
import facebook.FacebookCallbackListener;

public class AndroidFacebook implements CrossPlatformFacebook {
    
    private AndroidLauncher androidLauncher;
    private UiLifecycleHelper uiHelper;
    private Callback callback;
    private FacebookCallbackListener listener;

    public AndroidFacebook(AndroidLauncher androidLauncher, UiLifecycleHelper uiHelper) {
        this.androidLauncher = androidLauncher;
        this.uiHelper = uiHelper;
        this.callback = new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                String errorMessage = String.format("We were unable to connect to Facebook. Please check your network settings and try again.");
                listener.showErrorDialog(errorMessage);
                System.out.println(errorMessage);
            }
            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                System.out.println("Successfully shared to Facebook.");
            }
        };
    }
    
    public void setListener(FacebookCallbackListener listener) {
        this.listener = listener;
    }

    @Override
    public void post(String title, String description) {
        if (FacebookDialog.canPresentShareDialog(androidLauncher.getApplicationContext(), FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(androidLauncher)
            .setLink("http://www.numbercortex.com")
            .setPicture("http://www.numbercortex.com/images/number_cortex_mobile_banner.jpg")
            .setDescription(description)
            .setCaption(" ").setName(title).build();
            uiHelper.trackPendingDialogCall(shareDialog.present());
        } else {
            // Publish the post using the Feed Dialog
        }
    }
    
    public Callback getFacebookCallback() {
        return callback;
    }

}
