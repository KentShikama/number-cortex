package com.numbercortex.desktop;

<<<<<<< HEAD
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.numbercortex.Launch;
=======
import chartboost.ChartBoostListener;
import chartboost.CrossPlatformChartboost;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.numbercortex.view.Launch;
>>>>>>> f50a74ab4ac341496d302bf0305548565adea149

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
<<<<<<< HEAD
		new LwjglApplication(new Launch(), config);
=======
		config.title = "Number Cortex";
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
		new LwjglApplication(new Launch(chartboost), config);
>>>>>>> f50a74ab4ac341496d302bf0305548565adea149
	}
}
