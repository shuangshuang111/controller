package com.dongnaoedu.mall.service;

import java.math.BigDecimal;

import com.dongnaoedu.mall.pojo.common.DataTablesResult;
import com.dongnaoedu.mall.pojo.dto.OrderDetail;


/**
 * 
 */
public interface OrderService {

    /**
     * 获得订单列表
     * @param draw
     * @param start
     * @param length
     * @param search
     * @param orderCol
     * @param orderDir
     * @return
     */
    DataTablesResult getOrderList(int draw, int start, int length, String search, String orderCol, String orderDir);

    /**
     * 统计订单数
     * @return
     */
    Long countOrder();

    /**
     * 获取订单详情
     * @param orderId
     * @return
     */
    OrderDetail getOrderDetail(String orderId);

    /**
     * 发货
     * @param orderId
     * @param shippingName
     * @param shippingCode
     * @param postFee
     * @return
     */
    int deliver(String orderId,String shippingName,String shippingCode,BigDecimal postFee);

    /**
     * 备注
     * @param orderId
     * @param message
     * @return
     */
    int remark(String orderId, String message);

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    int cancelOrderByAdmin(String orderId);

    /**
     * 删除订单
     * @param id
     * @return
     */
    int deleteOrder(String id);

    /**
     * 定时取消订单
     */
    int cancelOrder();
}
