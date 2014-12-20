package com.numbercortex;

import iap.CrossPlatformIAP;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSPropertyList;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.bindings.chartboost.Chartboost;
import org.robovm.bindings.chartboost.ChartboostDelegateAdapter;
import org.robovm.bindings.facebook.dialogs.FBDialogs;
import org.robovm.bindings.facebook.dialogs.FBShareDialogParams;
import org.robovm.bindings.facebook.manager.FacebookManager;

import appleChartboost.AppleChartboost;
import appleFacebook.AppleFacebook;
import appleIAP.AppleIAP;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.numbercortex.view.Launch;

import facebook.CrossPlatformFacebook;

public class IOSLauncher extends IOSApplication.Delegate {

    private String appLink;
    private AppleChartboost crossPlatformChartboost;

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.orientationPortrait = true;
        config.orientationLandscape = false;
        
        appLink = "http://itunes.apple.com/app/id908897517";

        crossPlatformChartboost = new AppleChartboost();
        CrossPlatformFacebook crossPlatformFacebook = buildAppleFacebook();
        CrossPlatformIAP crossPlatformIAP = new AppleIAP();

        return new IOSApplication(new Launch(appLink, crossPlatformChartboost, crossPlatformFacebook, crossPlatformIAP), config);
    }

    private CrossPlatformFacebook buildAppleFacebook() {
        final String link = "http://www.numbercortex.com";
        FBShareDialogParams params = new FBShareDialogParams();
        params.setLink(new NSURL(link));
        CrossPlatformFacebook crossPlatformFacebook;
        if (FBDialogs.canPresentShareDialog(params)) {
            crossPlatformFacebook = new AppleFacebook();
        } else {
            crossPlatformFacebook = null;
        }
        return crossPlatformFacebook;
    }

    @Override
    public void didBecomeActive(UIApplication application) {
        super.didBecomeActive(application);
        String APP_ID = "53dc9c641873da4ec7b5a2b8";
        String APP_SIGNATURE = "ea716c2371dffac4ca1425817ef73df8ea17485b";
        Chartboost.start(APP_ID, APP_SIGNATURE, new ChartboostDelegateAdapter() {});
        Chartboost.cacheInterstitial("After Screen");
        FacebookManager.getInstance().handleDidBecomeActive(application);
    }

    @Override
    public boolean openURL(UIApplication application, NSURL url, String sourceApplication, NSPropertyList annotation) {
    	return FacebookManager.getInstance().handleOpenURL(application, url, sourceApplication, annotation);
    }

    @Override
    public void willTerminate(UIApplication application) {
    	FacebookManager.getInstance().handleWillTerminate(application);
    }

    public static void main(String[] argv) {
        try (NSAutoreleasePool pool = new NSAutoreleasePool()) {
            UIApplication.main(argv, null, IOSLauncher.class);
        }
    }
}