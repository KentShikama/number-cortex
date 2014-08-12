package com.numbercortex.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.numbercortex.view.Launch;

public class DesktopLauncher {

    static String appLink = "market://details?id=com.numbercortex.android";

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Number Cortex";
        new LwjglApplication(new Launch(appLink, null, null, null), config);
    }
}
