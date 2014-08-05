package androidFacebook;

import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.numbercortex.android.AndroidLauncher;
import facebook.CrossPlatformFacebook;

public class AndroidFacebook implements CrossPlatformFacebook {
    
    private AndroidLauncher androidLauncher;
    private UiLifecycleHelper uiHelper;

    public AndroidFacebook(AndroidLauncher androidLauncher, UiLifecycleHelper uiHelper) {
        this.androidLauncher = androidLauncher;
        this.uiHelper = uiHelper;
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

}
