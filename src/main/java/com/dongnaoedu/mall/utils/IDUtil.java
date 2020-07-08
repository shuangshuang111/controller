package com.dongnaoedu.mall.utils;

import java.util.Random;

/**
 * id 生成器
 * 
 * 
 */
public class IDUtil {

    /**
     * 随机id生成，使用雪花算法
     */
    public static long getRandomId() {
        SnowflakeIdWorker sf = new SnowflakeIdWorker();
        long id = sf.nextId();
        return id;
    }
	
    /**
     * 随机id生成，时间戳加上两位随机数，不能应对高并发
     */
    @Deprecated
    public static long getRandomId(int old) {
        long millis = System.currentTimeMillis();
        //加上两位随机数
        Random random = new Random();
        int end2 = random.nextInt(99);
        //如果不足两位前面补0
        String str = millis + String.format("%02d", end2);
        long id = new Long(str);
        return id;
    }
    
}

