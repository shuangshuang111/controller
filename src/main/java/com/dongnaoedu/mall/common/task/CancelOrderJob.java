package com.dongnaoedu.mall.common.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dongnaoedu.mall.mapper.TbOrderItemMapper;
import com.dongnaoedu.mall.mapper.TbOrderMapper;
import com.dongnaoedu.mall.mapper.TbOrderShippingMapper;
import com.dongnaoedu.mall.service.OrderService;

/**
 * 单机版订单失效清理
 * 
 * 
 */
@Component
public class CancelOrderJob {

    final static Logger log= LoggerFactory.getLogger(CancelOrderJob.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;

    /**
     * 每3个小时判断订单是否失效
     */
    @Scheduled(cron = "0 0 */3 * * ?")
    public void run() {

        log.info("开始执行自动取消订单定时任务");
        orderService.cancelOrder();
    }

    /**
     * 每月自动删除订单
     */
//    @Scheduled(cron = "0 0 0 0 1/1 ?")
//    public void deleteOrder() {
//
//        log.info("开始执行自动删除订单定时任务");
//        tbOrderMapper.deleteByExample(new TbOrderExample());
//        tbOrderItemMapper.deleteByExample(new TbOrderItemExample());
//        tbOrderShippingMapper.deleteByExample(new TbOrderShippingExample());
//    }
}
