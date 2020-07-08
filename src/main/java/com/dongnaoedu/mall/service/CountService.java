package com.dongnaoedu.mall.service;

import java.util.Date;
import java.util.List;

import com.dongnaoedu.mall.pojo.dto.OrderChartData;

/**
 * 
 */
public interface CountService {

    /**
     * 统计订单销量
     * @param type
     * @param startTime
     * @param endTime
     * @param year
     * @return
     */
    List<OrderChartData> getOrderCountData(int type, Date startTime, Date endTime, int year);
}
