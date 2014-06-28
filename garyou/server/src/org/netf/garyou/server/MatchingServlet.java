package org.netf.garyou.server;

import org.netf.garyou.shared.Message;
import org.netf.garyou.shared.MessageFactory;

import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;

import no.eirikb.gwtchannelapi.server.ChannelServer;

@SuppressWarnings("serial")
public class MatchingServlet extends ChannelServer {

	@Override
	protected void onJoin(String token, String channelName) {
	}

	@Override
	public void onMessage(String token, String channel, String message) {

		MessageFactory factory = AutoBeanFactorySource.create(MessageFactory.class);
		Message msg = factory.message().as();
		msg.setToken(message);

		send(channel, message);
	}

}
