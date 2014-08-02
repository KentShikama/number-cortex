package com.numbercortex.android;

<<<<<<< HEAD
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.numbercortex.Launch;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Launch(), config);
	}
=======
import android.app.Activity;
import android.os.Bundle;
import chartboost.AndroidChartboost;
import chartboost.CrossPlatformChartboost;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.numbercortex.view.Launch;
import com.chartboost.sdk.CBPreferences;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;

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
        this.cb.onCreate(this, appId, appSignature, (ChartboostDelegate) crossPlatformChartboost.getChartBoostDelegate());
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
        this.cb.onBackPressed();
        super.onBackPressed();
    }   
>>>>>>> f50a74ab4ac341496d302bf0305548565adea149
}
