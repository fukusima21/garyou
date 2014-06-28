package org.netf.garyou.shared;

import org.netf.garyou.shared.Message;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface MessageFactory extends AutoBeanFactory {
	AutoBean<Message> message();
}
