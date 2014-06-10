package org.netf.garyou.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;

import org.netf.garyou.garyouMain;

public class HtmlLauncher extends GwtApplication {

	@Override
	public GwtApplicationConfiguration getConfig() {

		GwtApplicationConfiguration config = new GwtApplicationConfiguration(480, 720);

		SimplePanel simplePanel = new SimplePanel();
		simplePanel.setStyleName("canvas-container");
		config.rootPanel = simplePanel;

		Element element = Document.get().getElementById("embed-" + GWT.getModuleName());
		element.appendChild(simplePanel.getElement());

		return config;
	}

	@Override
	public ApplicationListener getApplicationListener() {
		return new garyouMain();
	}
}