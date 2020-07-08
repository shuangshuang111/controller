package com.dongnaoedu.mall.common.shiro.cache;

import org.apache.shiro.cache.Cache;

/**
 * redis缓存管理
 * 
 * 
 *
 */
public class JedisShiroCacheManager implements ShiroCacheManager {

	private JedisManager jedisManager;

	public JedisManager getJedisManager() {
		return jedisManager;
	}

	public void setJedisManager(JedisManager jedisManager) {
		this.jedisManager = jedisManager;
	}

	@Override
	public <K, V> Cache<K, V> getCache(String name) {
		return new JedisShiroCache<K, V>(name, getJedisManager());
	}

	@Override
	public void destroy() {
		//如果和其他系统，或者应用在一起就不能关闭
        //getJedisManager().getJedis().shutdown();
	}

}
