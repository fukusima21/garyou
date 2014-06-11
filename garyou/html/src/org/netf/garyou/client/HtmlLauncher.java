package org.netf.garyou.client;

import javax.jws.soap.SOAPBinding.Style;

import org.netf.garyou.garyouMain;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HtmlLauncher extends GwtApplication {

	static HtmlLauncher instance;

	@Override
	public GwtApplicationConfiguration getConfig() {

		GwtApplicationConfiguration config = new GwtApplicationConfiguration(480, 720);

		config.canvasId = "main";

		Element element = Document.get().getElementById("embed-" + GWT.getModuleName());
		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("100%");
		panel.setHeight("100%");
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		element.appendChild(panel.getElement());
		config.rootPanel = panel;

		return config;
	}

	@Override
	public ApplicationListener getApplicationListener() {

		instance = this;
		setLogLevel(LOG_NONE);
		setLoadingListener(new LoadingListener() {
			@Override
			public void beforeSetup() {

			}

			@Override
			public void afterSetup() {
				scaleCanvas();
				setupResizeHook();
			}
		});

		return new garyouMain();
	}

	private void scaleCanvas() {

		int innerWidth = getWindowInnerWidth();
		int innerHeight = getWindowInnerHeight();
		int newWidth = innerWidth;
		int newHeight = innerHeight;
		float ratio = innerWidth / (float) innerHeight;
		float viewRatio = 480.0f / 720.0f;

		if (ratio > viewRatio) {
			newWidth = (int) (innerHeight * viewRatio);
		} else {
			newHeight = (int) (innerWidth / viewRatio);
		}

		NodeList<Element> canvasList = Document.get().getElementsByTagName("canvas");

		if (canvasList != null && canvasList.getLength() > 0) {
			Element canvas = canvasList.getItem(0);
			canvas.setAttribute("width", "" + newWidth + "px");
			canvas.setAttribute("height", "" + newHeight + "px");
			canvas.getStyle().setWidth(newWidth, Unit.PX);
			canvas.getStyle().setHeight(newHeight, Unit.PX);
			canvas.getStyle().setTop((int) ((innerHeight - newHeight) * 0.5f), Unit.PX);
			canvas.getStyle().setLeft((int) ((innerWidth - newWidth) * 0.5f), Unit.PX);
			canvas.getStyle().setPosition(Position.ABSOLUTE);
		}
	}

	native int getWindowInnerWidth() /*-{
										return $wnd.innerWidth;
										}-*/;

	native int getWindowInnerHeight() /*-{
										return $wnd.innerHeight;
										}-*/;

	native void setupResizeHook() /*-{
									var htmlLauncher_onWindowResize = $entry(@org.netf.garyou.client.HtmlLauncher::handleResize());
									$wnd.addEventListener('resize', htmlLauncher_onWindowResize, false);
									}-*/;

	public static void handleResize() {
		instance.scaleCanvas();
	}

}