package com.fogok.explt.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.fogok.explt.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        final boolean issF = true;

        if (issF){
            config.width = 854;
            config.height = 480;
        }else{
            config.width = 1366;
            config.height = 768;
        }


        config.fullscreen = false;
        Main.ISPC = true;


		new LwjglApplication(new Main(), config);
	}
}
