package com.numbercortex.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.numbercortex.view.Launch;

public class DesktopLauncher {

    static String appLink = "http://play.google.com/store/apps/details?id=com.numbercortex.android";

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Number Cortex";
        new LwjglApplication(new Launch(appLink, null), config);
    }
}
