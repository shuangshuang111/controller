package com.dongnaoedu.mall.common.shiro.sesion;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.session.Session;

import com.dongnaoedu.mall.common.shiro.cache.JedisManager;

public class JedisShiroSessionRepository implements ShiroSessionRepository {

	public static final String REDIS_SHIRO_SESSION = "mall-shiro-session:";
	
	public static final String REDIS_SHIRO_ALL = "*mall-shiro-session:*";
	
	private static final int SESSION_VAL_TIME_SPAN = 18000;
	
	private static final int DB_INDEX = 2;
	

	private JedisManager jedisManager;

	public JedisManager getJedisManager() {
		return jedisManager;
	}

	public void setJedisManager(JedisManager jedisManager) {
		this.jedisManager = jedisManager;
	}

	private String buildRedisSessionKey(Serializable sessionId) {
		return REDIS_SHIRO_SESSION + sessionId;
	}

	@Override
	public void saveSession(Session session) {
		if (session == null || session.getId() == null)
			throw new NullPointerException("session is empty");
		try {
			byte[] key = SerializeUtil.serialize(buildRedisSessionKey(session.getId()));

			byte[] value = SerializeUtil.serialize(session);

			/*
			 * 直接使用 (int) (session.getTimeout() / 1000) 的话，session失效和redis的TTL 同时生效
			 */
			getJedisManager().saveValueByKey(DB_INDEX, key, value, (int) (session.getTimeout() / 1000));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteSession(Serializable sessionId) {
		if (sessionId == null) {
			throw new NullPointerException("session id is empty");
		}
		try {
			getJedisManager().deleteByKey(DB_INDEX, SerializeUtil.serialize(buildRedisSessionKey(sessionId)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Session getSession(Serializable sessionId) {
		if (sessionId == null)
			throw new NullPointerException("session id is empty");
		Session session = null;
		try {
			 byte[] value = getJedisManager().getValueByKey(DB_INDEX, SerializeUtil
	                    .serialize(buildRedisSessionKey(sessionId)));
	         session = SerializeUtil.deserialize(value, Session.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return session;
	}

	@Override
	public Collection<Session> getAllSessions() {
		Collection<Session> sessions = null;
		try {
			sessions = getJedisManager().AllSession(DB_INDEX, REDIS_SHIRO_SESSION);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sessions;
	}

}
