package org.netf.garyou.client;

import org.netf.garyou.WebRtcEvent;
import org.netf.garyou.garyouMain;
import org.netf.garyou.screens.MenuScreen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HtmlLauncher extends GwtApplication {

	static HtmlLauncher instance;

	private WebRtcResolverImpl webRtcResolverImpl;
	private garyouMain game;

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

		webRtcResolverImpl = new WebRtcResolverImpl();

		game = new garyouMain(webRtcResolverImpl);

		return game;

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

	native void disconnect() /*-{
								$wnd.connection.close();
								$wnd.peer.disconnect();
								}-*/;

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

	private static DialogBox dialogbox;

	public static void showDialog(String id) {

		String href = Window.Location.getHref();

		String url = "http://garyou14.appspot.com/vs?" + id;

		if (href.contains("/localhost")) {
			url = "http://localhost:8080/html/vs?" + id;
		}

		dialogbox = new DialogBox(false);
		dialogbox.setText("対決モード");

		VerticalPanel dialogBoxContents = new VerticalPanel();
		HTML message = new HTML("<center><br />" + "対戦者に以下のURLを送り、相手が接続するまでお待ちください。<br /><br />" + url + "<br />" + "<br /></center>");

		Button button = new Button("キャンセルしてメニューへ", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogbox.hide();
				instance.game.setScreen(new MenuScreen(instance.game));
			}
		});

		SimplePanel holder = new SimplePanel();
		holder.add(button);

		dialogBoxContents.add(message);
		dialogBoxContents.add(holder);
		dialogbox.setWidget(dialogBoxContents);

		dialogbox.center();

	}

	public static void hideDialog() {
		dialogbox.hide();
	}

	public static void onRecv(String message) {

		if (instance.webRtcResolverImpl != null) {
			WebRtcEvent webRtcEvent = instance.webRtcResolverImpl.getWebRtcEvent();
			if (webRtcEvent != null) {
				webRtcEvent.onRecv(message);
			}
		}

	}
}