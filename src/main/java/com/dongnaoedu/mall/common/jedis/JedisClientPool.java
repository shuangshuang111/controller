package com.dongnaoedu.mall.common.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

public class JedisClientPool implements JedisClient {
	
	private JedisPool jedisPool;

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	@Override
	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		String result = null;
		try {
			result = jedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return result;
	}

	@Override
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		String result = null;
		try {
			result = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return result;
	}

	@Override
	public Boolean exists(String key) {
		Jedis jedis = jedisPool.getResource();
		Boolean result = false;
		try {
			result = jedis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return result;
	}

	@Override
	public Long expire(String key, int seconds) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try {
			result = jedis.expire(key, seconds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return result;
	}

	@Override
	public Long ttl(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try {
			result = jedis.ttl(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return result;
	}

	@Override
	public Long incr(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try {
			result = jedis.incr(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return result;
	}

	@Override
	public Long hset(String key, String field, String value) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try {
			result = jedis.hset(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return result;
	}

	@Override
	public String hget(String key, String field) {
		Jedis jedis = jedisPool.getResource();
		String result = null;
		try {
			result = jedis.hget(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return result;
	}

	@Override
	public Long hdel(String key, String... field) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try {
			result = jedis.hdel(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return result;
	}

	@Override
	public Boolean hexists(String key, String field) {
		Jedis jedis = jedisPool.getResource();
		Boolean result = false;
		try {
			result = jedis.hexists(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return result;
	}

	@Override
	public List<String> hvals(String key) {
		Jedis jedis = jedisPool.getResource();
		List<String> result = null;
		try {
			result = jedis.hvals(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return result;
	}

	@Override
	public Long del(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try {
			result = jedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return result;
	}

}
