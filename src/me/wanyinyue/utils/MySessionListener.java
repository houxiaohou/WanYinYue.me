package me.wanyinyue.utils;

import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;

@Component(value="mySessionListener")
public class MySessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		MySessionContext.addSession(httpSessionEvent.getSession());
		//session的生命周期为2个小时
		httpSessionEvent.getSession().setMaxInactiveInterval(2*60*60);
		System.out.println(new Date() +"创建了session"+httpSessionEvent.getSession().getId());
	}

	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		HttpSession session = httpSessionEvent.getSession();
		MySessionContext.delSession(session);
		System.out.println(new Date() +"销毁了session"+httpSessionEvent.getSession().getId());
	}
}
