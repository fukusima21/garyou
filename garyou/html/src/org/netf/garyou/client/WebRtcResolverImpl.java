package org.netf.garyou.client;

import java.util.ArrayList;
import java.util.List;

import org.netf.garyou.WebRtcResolver;

import com.badlogic.gdx.Gdx;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

public class WebRtcResolverImpl implements WebRtcResolver {

	private static final String KEY_CODE = "45e414ba-f60d-11e3-a56e-71121dc33c38";
	private static final String LOCAL_KEY_CODE = "6165842a-5c0d-11e3-b514-75d3313b9d05";

	private static String ACTIVE_LIST_URL = "https://skyway.io/active/count/";

	@Override
	public void setup() {

		String href = Window.Location.getHref();

		if (href.contains("localhost")) {
			setupPeer(LOCAL_KEY_CODE);
			ACTIVE_LIST_URL = ACTIVE_LIST_URL + LOCAL_KEY_CODE;
		} else {
			setupPeer(KEY_CODE);
			ACTIVE_LIST_URL = ACTIVE_LIST_URL + KEY_CODE;
		}

		Timer timer = new Timer() {
			@Override
			public void run() {
				requestIdList();
			}
		};

		timer.scheduleRepeating(5000);

	}

	@Override
	public String getId() {
		return getPeerId();
	}

	@Override
	public List<String> getidList() {

		List<String> resultList = new ArrayList<String>();

		JsArrayString peerIdList = (JsArrayString) getPeerIdList();

		int length = peerIdList.length();
		for (int i = 0; i < length; i++) {
			resultList.add(peerIdList.get(i));
		}

		return resultList;
	}

	private void requestIdList() {

		// Send request to server and catch any errors.
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, ACTIVE_LIST_URL);

		try {
			builder.sendRequest(null, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {

						String text = response.getText();

						Gdx.app.log("getId=", getId());
						Gdx.app.log("", text);

						setPeerIdList(JsonUtils.safeEval(text));
					}
				}

				@Override
				public void onError(Request request, Throwable exception) {
				}
			});
		} catch (RequestException e) {
		}
	}

	private native void setupPeer(String keyCode) /*-{
													var param1;
													var param2 = {"key": keyCode};

													$wnd.peer = new $wnd.Peer(param1,param2);

													$wnd.peer.on('open', function(id) {
														$wnd.myid = id;
													});

													$wnd.peer.on('connection', function(conn) {
													;
													});

													}-*/;

	private native String getPeerId() /*-{
										return $wnd.myid;
										}-*/;

	private native void setPeerIdList(JavaScriptObject idList) /*-{
																$wnd.idList = idList;
																}-*/;

	private native JsArrayString getPeerIdList() /*-{
													return $wnd.idList;
													}-*/;

}
