package com.numbercortex.android;

import android.content.Intent;
import android.os.Bundle;
import androidChartboost.AndroidChartboost;
import androidFacebook.AndroidFacebook;
import chartboost.CrossPlatformChartboost;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.numbercortex.view.Launch;
import com.chartboost.sdk.CBPreferences;
import com.chartboost.sdk.Chartboost;
import com.facebook.UiLifecycleHelper;
import facebook.CrossPlatformFacebook;

public class AndroidLauncher extends AndroidApplication {

    private Chartboost cb;
    private UiLifecycleHelper uiHelper;
    private CrossPlatformFacebook crossPlatformFacebook;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        this.cb = Chartboost.sharedChartboost();
        String appId = "53db59161873da6e205eaf25";
        String appSignature = "2423da041b609b5625b427b94f514ecc10a515b1";
        CBPreferences.getInstance().setImpressionsUseActivities(true);
        CrossPlatformChartboost crossPlatformChartboost = new AndroidChartboost(cb, this);
        this.cb.onCreate(this, appId, appSignature, ((AndroidChartboost) crossPlatformChartboost).getChartBoostDelegate());
        
        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);
        crossPlatformFacebook = new AndroidFacebook(this, uiHelper);
        initialize(new Launch(crossPlatformChartboost, crossPlatformFacebook), config);
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
