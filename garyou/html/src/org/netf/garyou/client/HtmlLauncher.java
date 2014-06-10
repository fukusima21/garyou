package org.netf.garyou.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import org.netf.garyou.garyouMain;

public class HtmlLauncher extends GwtApplication {

	@Override
	public GwtApplicationConfiguration getConfig() {
		return new GwtApplicationConfiguration(480, 720);
	}

	@Override
	public ApplicationListener getApplicationListener() {
		return new garyouMain();
	}
}