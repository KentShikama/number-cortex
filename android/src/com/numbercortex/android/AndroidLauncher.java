package com.numbercortex.android;

import android.content.Intent;
import android.os.Bundle;
import androidChartboost.AndroidChartboost;
import chartboost.CrossPlatformChartboost;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.numbercortex.view.Launch;
import com.chartboost.sdk.CBPreferences;
import com.chartboost.sdk.Chartboost;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.model.OpenGraphAction;
import com.facebook.model.OpenGraphObject;
import com.facebook.widget.FacebookDialog;

public class AndroidLauncher extends AndroidApplication {

    private Chartboost cb;
    private UiLifecycleHelper uiHelper;
    // private StatusCallback statusCallback = new Session.StatusCallback() {
    // @Override
    // public void call(Session session, SessionState state, Exception exception) {
    //
    // }
    // };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.cb = Chartboost.sharedChartboost();
        String appId = "53db59161873da6e205eaf25";
        String appSignature = "2423da041b609b5625b427b94f514ecc10a515b1";
        CBPreferences.getInstance().setImpressionsUseActivities(true);
        CrossPlatformChartboost crossPlatformChartboost = new AndroidChartboost(cb, this);
        this.cb.onCreate(this, appId, appSignature, ((AndroidChartboost) crossPlatformChartboost).getChartBoostDelegate());
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new Launch(crossPlatformChartboost), config);

        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);

        if (FacebookDialog.canPresentShareDialog(getApplicationContext(), FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
            .setLink("http://www.numbercortex.com")
            .setPicture("http://www.numbercortex.com/images/number_cortex_mobile_banner.jpg")
            .setDescription("Interested in puzzle games? Challenge yourself with the new two player board game, Number Cortex.")
            .setCaption(" ").setName("Level 8 Cleared!").build();
            uiHelper.trackPendingDialogCall(shareDialog.present());
        } else {
            // Publish the post using the Feed Dialog
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                System.out.println(String.format("Error: %s", error.toString()));
            }
            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                System.out.println("Success!");
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }
    @Override
    protected void onDestroy() {
        this.cb.onDestroy(this);
        super.onDestroy();
        uiHelper.onDestroy();
    }
    @Override
    protected void onStart() {
        super.onStart();
        this.cb.onStart(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        this.cb.onStop(this);
    }
    @Override
    public void onBackPressed() {
        if (this.cb.onBackPressed()) {
            return;
        } else {
            super.onBackPressed();
        }
    }
}
