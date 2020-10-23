package com.team1091.fighterai.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.team1091.fighterai.ConfigKt;
import com.team1091.fighterai.FighterAIGame;

public class DesktopLauncher {
    public static void main(String[] arg) {

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.useVsync(true);

        if (ConfigKt.getFullScreen()) {
            Graphics.Monitor primary = Lwjgl3ApplicationConfiguration.getMonitors()[1];
            Graphics.DisplayMode desktopMode = Lwjgl3ApplicationConfiguration.getDisplayMode(primary);
            config.setFullscreenMode(desktopMode);
        }
        new Lwjgl3Application(new FighterAIGame(), config);
    }
}
