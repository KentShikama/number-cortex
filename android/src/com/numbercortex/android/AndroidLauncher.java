package com.numbercortex.android;

import android.os.Bundle;
import androidChartboost.AndroidChartboost;
import chartboost.CrossPlatformChartboost;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.numbercortex.view.Launch;
import com.chartboost.sdk.CBPreferences;
import com.chartboost.sdk.Chartboost;

public class AndroidLauncher extends AndroidApplication {

    private Chartboost cb;

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
    protected void onDestroy() {
        this.cb.onDestroy(this);
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        if (this.cb.onBackPressed()) {
            return;
        }
        else {
            super.onBackPressed();            
        }
    }   
}
