package com.dongnaoedu.mall.common.shiro.sesion;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

/**
 * shiro 回话 监听
 */
public class CustomSessionListener implements SessionListener {

	private ShiroSessionRepository shiroSessionRepository;
	
    public ShiroSessionRepository getShiroSessionRepository() {
        return shiroSessionRepository;
    }

    public void setShiroSessionRepository(ShiroSessionRepository shiroSessionRepository) {
        this.shiroSessionRepository = shiroSessionRepository;
    }
    
    /**
     * 失效
     */
	@Override
	public void onExpiration(Session session) {
		shiroSessionRepository.deleteSession(session.getId());
	}
	
    /**
     * 一个回话的生命周期开始
     */
	@Override
	public void onStart(Session session) {
		System.out.println("on start, session id : " + session.getId());
	}

    /**
     * 一个回话的生命周期结束
     */
	@Override
	public void onStop(Session session) {
		System.out.println("on stop, seesion id : " + session.getId());
	}

}
