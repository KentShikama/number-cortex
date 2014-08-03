package com.numbercortex;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.bindings.chartboost.Chartboost;
import org.robovm.bindings.chartboost.ChartboostDelegateAdapter;
import appleChartboost.AppleChartboost;
import chartboost.CrossPlatformChartboost;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.numbercortex.view.Launch;

public class IOSLauncher extends IOSApplication.Delegate {
    private Chartboost chartboost;
    private AppleChartboost crossPlatformChartboost;

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.orientationPortrait = true;
        config.orientationLandscape = false;
        crossPlatformChartboost = new AppleChartboost();
        return new IOSApplication(new Launch(crossPlatformChartboost), config);
    }
    
    @Override
    public void didBecomeActive (UIApplication application) {
        super.didBecomeActive(application);
        chartboost = Chartboost.sharedChartboost();
        chartboost.setAppId("53dc9c641873da4ec7b5a2b8");
        chartboost.setAppSignature("ea716c2371dffac4ca1425817ef73df8ea17485b");
        chartboost.startSession();
        crossPlatformChartboost.setChartBoost(chartboost);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}