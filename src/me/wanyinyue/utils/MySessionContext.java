package me.wanyinyue.utils;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

@Component(value = "mySessionContext")
public class MySessionContext {
	private static HashMap<String, HttpSession> sessionContextMap = new HashMap<String, HttpSession>();

	public static synchronized void addSession(HttpSession session) {
		if (session != null) {
			sessionContextMap.put(session.getId(), session);
		}
	}

	public static synchronized void delSession(HttpSession session) {
		if (session != null) {
			sessionContextMap.remove(session.getId());
		}
	}

	public static synchronized HttpSession getSession(String session_id) {
		if (session_id == null) {
			return null;
		}
		return (HttpSession) sessionContextMap.get(session_id);
	}
}
