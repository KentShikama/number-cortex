package com.numbercortex.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import chartboost.ChartBoostListener;
import chartboost.CrossPlatformChartboost;
import com.numbercortex.view.Launch;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Number Cortex";
	        CrossPlatformChartboost chartboost = new CrossPlatformChartboost() {
	            @Override
	            public void showMoreApps() {}
	            @Override
	            public void setListener(ChartBoostListener listener) {}        
	        };
	        
		new LwjglApplication(new Launch(chartboost), config);
	}
}
