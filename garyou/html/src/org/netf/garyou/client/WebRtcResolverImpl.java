package org.netf.garyou.client;

import org.netf.garyou.WebRtcEvent;
import org.netf.garyou.WebRtcResolver;

import com.google.gwt.user.client.Window;

public class WebRtcResolverImpl implements WebRtcResolver {

	public WebRtcResolverImpl() {
	}

	@Override
	public void init() {
		initPeer();
	}

	private WebRtcEvent event;

	native void initPeer() /*-{
							$wnd.peer = new $wnd.Peer(null,{key: '45e414ba-f60d-11e3-a56e-71121dc33c38'});

							$wnd.peer.on('open', function(id) {
								$wnd.peerId = id;
								@org.netf.garyou.client.HtmlLauncher::showDialog(Ljava/lang/String;)(id);
							});

							$wnd.peer.on('connection', function(conn) {

								conn.on('data', function(data) {

									$wnd.connection = conn;

									if (data == 'start') {
										@org.netf.garyou.client.HtmlLauncher::hideDialog()();
										$wnd.connection.send('start');
									}

									@org.netf.garyou.client.HtmlLauncher::onRecv(Ljava/lang/String;)(data);

									console.log(data);

								});

							});

							}-*/;

	@Override
	public boolean isPlayer1() {

		String href = Window.Location.getHref();

		if (href.contains("/vs")) {
			return false;
		}

		return true;
	}

	@Override
	public String getConnectionIdFromUrl() {

		String href = Window.Location.getHref();

		int pos = href.indexOf('?');

		if (pos > -1) {
			return href.substring(pos + 1);
		}

		return null;
	}

	public void connect(String id) {
		connectPeer(id);
	}

	native void connectPeer(String id) /*-{
										$wnd.peer = new $wnd.Peer(null,{key: '45e414ba-f60d-11e3-a56e-71121dc33c38'});

										$wnd.connection = $wnd.peer.connect(id);

										$wnd.connection.on('open', function() {
											$wnd.connection.send('start');
										});

										$wnd.connection.on('data', function(data) {
											@org.netf.garyou.client.HtmlLauncher::onRecv(Ljava/lang/String;)(data);
											console.log(data);
										});

										}-*/;

	@Override
	public void sendMessage(String message) {
		sendMessagePeer(message);
	}

	native void sendMessagePeer(String message) /*-{
												console.log("send data = " + message + " " + $wnd.connection);
												$wnd.connection.send(message);
												}-*/;

	@Override
	public void setWebRtcEvent(WebRtcEvent event) {
		this.event = event;
	}

	@Override
	public WebRtcEvent getWebRtcEvent() {
		return event;
	}

}
