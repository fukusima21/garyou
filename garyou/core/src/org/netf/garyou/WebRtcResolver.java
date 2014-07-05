package org.netf.garyou;

public interface WebRtcResolver {
	void init();

	boolean isPlayer1();

	String getConnectionIdFromUrl();

	void connect(String id);

	void sendMessage(String message);

	void setWebRtcEvent(WebRtcEvent event);

	WebRtcEvent getWebRtcEvent();

}
