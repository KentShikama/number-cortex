package com.numbercortex;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSPropertyList;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.bindings.facebook.dialogs.FBDialogs;
import org.robovm.bindings.facebook.dialogs.FBShareDialogParams;
import org.robovm.bindings.facebook.manager.FacebookManager;

import appleFacebook.AppleFacebook;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.numbercortex.view.Launch;

import facebook.CrossPlatformFacebook;

public class IOSLauncher extends IOSApplication.Delegate {

    private String appLink;
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.orientationPortrait = true;
        config.orientationLandscape = false;
        
        appLink = "http://itunes.apple.com/app/id908897517";

        CrossPlatformFacebook crossPlatformFacebook = buildAppleFacebook();
        return new IOSApplication(new Launch(appLink, crossPlatformFacebook), config);
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