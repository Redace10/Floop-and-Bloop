package com.floopandbloop.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.floopandbloop.game.AdHandler;
import com.floopandbloop.game.PlayServices;

public class HtmlLauncher extends GwtApplication {

        AdHandler adHandler;
        PlayServices playServices;

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new com.floopandbloop.game.MainGame(adHandler, playServices);
        }
}