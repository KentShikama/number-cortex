package com.numbercortex.android;

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
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

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
        Session.openActiveSession(this, true, new Session.StatusCallback() {
            // callback when session changes state
            @Override
            public void call(Session session, SessionState state, Exception exception) {
              if (session.isOpened()) {

                // make request to the /me API
                Request.newMeRequest(session, new Request.GraphUserCallback() {

                  // callback after Graph API response with user object
                  @Override
                  public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        System.out.println("Connected");
                        System.out.println("Connected");
                        System.out.println("Connected");
                        System.out.println("Connected");
                        System.out.println("Connected");
                    }
                  }
                }).executeAsync();
              }
            }
          });
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
