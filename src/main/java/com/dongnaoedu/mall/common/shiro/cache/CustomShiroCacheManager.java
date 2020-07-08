package com.dongnaoedu.mall.common.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;

/**
 * 缓存管理
 * 
 * 
 *
 */
public class CustomShiroCacheManager implements CacheManager, Destroyable {

	private ShiroCacheManager shiroCacheManager;
	
	public ShiroCacheManager getShiroCacheManager() {
		return shiroCacheManager;
	}

	public void setShiroCacheManager(ShiroCacheManager shiroCacheManager) {
		this.shiroCacheManager = shiroCacheManager;
	}

	@Override
	public void destroy() throws Exception {
		shiroCacheManager.destroy();
	}

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		return getShiroCacheManager().getCache(name);
	}

}
