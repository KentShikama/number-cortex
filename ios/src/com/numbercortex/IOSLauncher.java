package com.numbercortex;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import chartboost.ChartBoostListener;
import chartboost.CrossPlatformChartboost;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.numbercortex.view.Launch;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.orientationPortrait = true;
        config.orientationLandscape = false;
        CrossPlatformChartboost chartboost = new CrossPlatformChartboost() {
            @Override
            public void showMoreApps() {}
            @Override
            public Object getChartBoostDelegate() {
                return null;
            }
            @Override
            public void setListener(ChartBoostListener listener) {}        
        };
        return new IOSApplication(new Launch(chartboost), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}