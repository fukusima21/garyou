package org.netf.garyou.client;

import no.eirikb.gwtchannelapi.client.Channel;
import no.eirikb.gwtchannelapi.client.ChannelListener;

import org.netf.garyou.WebRtcResolver;
import org.netf.garyou.shared.Message;
import org.netf.garyou.shared.MessageFactory;

import com.badlogic.gdx.Gdx;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class WebRtcResolverImpl implements WebRtcResolver {

	private Channel channel = new Channel("test");

	@Override
	public void setup() {

		channel.addChannelListener(new ChannelListener() {

			@Override
			public void onOpen() {
			}

			@Override
			public void onMessage(String json) {

				MessageFactory factory = GWT.create(MessageFactory.class);

				AutoBean<Message> bean = AutoBeanCodex.decode(factory, Message.class, json);

				Message message = bean.as();

				Gdx.app.log("token ---> ", message.getToken());
			}

			@Override
			public void onError(int code, String description) {
			}

			@Override
			public void onClose() {
			}
		});

		channel.join();

	}

	@Override
	public void sendMessage(String message) {
		channel.send(message);
	}

}
