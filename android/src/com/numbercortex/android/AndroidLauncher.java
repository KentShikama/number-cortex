package com.numbercortex.android;

import iap.AndroidIAP;
import iap.CrossPlatformIAP;
import util.IabHelper;
import util.IabResult;
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
    private IabHelper iabHelper;

    String licenseKey = buildLicenseKey();
    
    String appLink = "market://details?id=com.numbercortex.android";
    
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
        
        iabHelper = new IabHelper(this, licenseKey);
        iabHelper.enableDebugLogging(true);
        final CrossPlatformIAP crossPlatformIAP = new AndroidIAP(this, iabHelper);
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
               if (!result.isSuccess()) {
                  System.out.println("Problem setting up In-app Billing: " + result);
               }
               System.out.println("Successfully connected.");
            }
         });
        initialize(new Launch(appLink, crossPlatformChartboost, crossPlatformFacebook, crossPlatformIAP), config);
    }
    
    private String buildLicenseKey() {
        StringBuilder a = new StringBuilder("BIIM");
        String b = "IjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmziEv4y6AylCtU/";
        String c = "7UHkp/Lk4Sm6LlH82HMOTZGYUWgh9w5VcvN/";
        String d = "l9ExNuGzHp4BOLEMV3LFVtUmMD47Koh0AEF2VBr9JL";
        String e = "xjM2Ybnvd182QAfI2zY4YhXf6vLPLDFkL2MGN8X7Yc7IQ12DUrOg8OFOq+URoGuu5vO6CD3oguIDnyxueFUatSft/";
        String f = "ZFilYWGQ2b8b0GnnOyLkEGgrnQXRgMYRle5KUget2VnUUUTIiQHwOGZmERrRIVcjHMzCyHG+sI6W3a8+Mzcq8vxutaKFOPVUjsiDtgPO7isVPpb8HVEAAx+zwT69eeOuofT0WbjQj1tSeP45vZcUfjAi8eaGkV2QIDAQAB";
        return a.reverse() + b + c + d + e + f;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, ((AndroidFacebook) crossPlatformFacebook).getFacebookCallback());
        if (iabHelper != null) {
            if (iabHelper.handleActivityResult(requestCode, resultCode, data)) {
                System.out.println("onActivityResult handled by IABUtil.");
            }
        }
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
        if (iabHelper != null) {
            iabHelper.dispose();
        }
        iabHelper = null;
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
