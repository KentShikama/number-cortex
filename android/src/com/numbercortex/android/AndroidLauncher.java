package com.numbercortex.android;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.facebook.UiLifecycleHelper;
import com.numbercortex.view.Launch;

import androidFacebook.AndroidFacebook;
import facebook.CrossPlatformFacebook;

public class AndroidLauncher extends AndroidApplication {
    private UiLifecycleHelper uiHelper;
    private CrossPlatformFacebook crossPlatformFacebook;

    String appLink = "market://details?id=com.numbercortex.android";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);
        crossPlatformFacebook = new AndroidFacebook(this, uiHelper);
        
        initialize(new Launch(appLink, crossPlatformFacebook), config);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, ((AndroidFacebook) crossPlatformFacebook).getFacebookCallback());
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
}
