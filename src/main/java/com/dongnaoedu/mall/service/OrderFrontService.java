package com.dongnaoedu.mall.service;


import com.dongnaoedu.mall.pojo.TbThanks;
import com.dongnaoedu.mall.pojo.front.Order;
import com.dongnaoedu.mall.pojo.front.OrderInfo;
import com.dongnaoedu.mall.pojo.front.PageOrder;


/**
 * 
 */
public interface OrderFrontService {

    /**
     * 分页获得用户订单
     * @param userId
     * @param page
     * @param size
     * @return
     */
    PageOrder getOrderList(Long userId, int page, int size);

    /**
     * 获得单个订单
     * @param orderId
     * @return
     */
    Order getOrder(String orderId);

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    int cancelOrder(String orderId);

    /**
     * 创建订单
     * @param orderInfo
     * @return
     */
    String createOrder(OrderInfo orderInfo);

    /**
     * 删除订单
     * @param orderId
     * @return
     */
    int delOrder(String orderId);

    /**
     * 订单支付 生成捐赠数据
     * @param tbThanks
     * @return
     */
    int payOrder(TbThanks tbThanks);

    /**
     * 支付审核通过
     * @param tokenName
     * @param token
     * @param id
     * @return
     */
    int passPay(String tokenName,String token,String id);

    /**
     * 支付审核驳回
     * @param tokenName
     * @param token
     * @param id
     * @return
     */
    int backPay(String tokenName,String token,String id);

    /**
     * 支付审核通过不显示
     * @param tokenName
     * @param token
     * @param id
     * @return
     */
    int notShowPay(String tokenName,String token,String id);

    /**
     * 捐赠编辑
     * @param tokenName
     * @param token
     * @param tbThanks
     * @return
     */
    int editPay(String tokenName,String token,TbThanks tbThanks);

    /**
     * 捐赠删除不回邮件
     * @param tokenName
     * @param token
     * @param id
     * @return
     */
    int payDelNotNotify(String tokenName,String token,String id);

    /**
     * 捐赠删除
     * @param tokenName
     * @param token
     * @param id
     * @return
     */
    int payDel(String tokenName,String token,String id);
}
