package androidFacebook;

import android.os.Bundle;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.FacebookDialog.Callback;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.numbercortex.android.AndroidLauncher;
import facebook.CrossPlatformFacebook;
import facebook.FacebookCallbackListener;

public class AndroidFacebook implements CrossPlatformFacebook {

    private AndroidLauncher androidLauncher;
    private UiLifecycleHelper uiHelper;
    private Callback callback;
    private FacebookCallbackListener listener;
    private OnCompleteListener feedDialogOnCompleteListener;

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
        this.feedDialogOnCompleteListener = new OnCompleteListener() {
            @Override
            public void onComplete(Bundle values, FacebookException error) {
                if (error == null) {
                    final String postId = values.getString("post_id");
                    if (postId != null) {
                        System.out.println("Successfully posted to Facebook: " + postId);
                    } else {
                        System.out.println("Cancelled posting to Facebook.");
                    }
                } else if (error instanceof FacebookOperationCanceledException) {
                    System.out.println("User aborted from the posting process.");
                } else {
                    String errorMessage = String.format("We were unable to connect to Facebook. Please check your network settings and try again.");
                    listener.showErrorDialog(errorMessage);
                    System.out.println(errorMessage);
                }
            }
        };
    }

    @Override
    public void setListener(FacebookCallbackListener listener) {
        this.listener = listener;
    }

    @Override
    public void post(final String title, final String description) {
        final String link = "http://www.numbercortex.com";
        final String pictureLink = "http://www.numbercortex.com/images/number_cortex_mobile_banner.jpg";
        final String caption = " ";
        if (FacebookDialog.canPresentShareDialog(androidLauncher.getApplicationContext(), FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(androidLauncher).setLink(link).setPicture(pictureLink).setDescription(description).setCaption(caption).setName(title).build();
            uiHelper.trackPendingDialogCall(shareDialog.present());
        } else {
            if (Session.getActiveSession() != null && Session.getActiveSession().isOpened()) {
                showFeedDialog(title, description, link, pictureLink, caption);
            } else {
                Session.openActiveSession(androidLauncher, true, new StatusCallback() {
                    @Override
                    public void call(Session session, SessionState state, Exception exception) {
                        if (state.isOpened()) {
                            showFeedDialog(title, description, link, pictureLink, caption);
                        }
                        if (exception != null) {
                            String errorMessage = String.format("We were unable to connect to Facebook. Please check your network settings and try again.");
                            listener.showErrorDialog(errorMessage);
                            System.out.println(errorMessage);
                        }
                    }
                });
            }
        }
    }
    private void showFeedDialog(final String title, final String description, final String link, final String pictureLink, final String caption) {
        Bundle params = new Bundle();
        params.putString("name", title);
        params.putString("caption", caption);
        params.putString("description", description);
        params.putString("link", link);
        params.putString("picture", pictureLink);
        WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(androidLauncher, Session.getActiveSession(), params)).setOnCompleteListener(feedDialogOnCompleteListener).build();
        feedDialog.show();
    }

    public Callback getFacebookCallback() {
        return callback;
    }

}
