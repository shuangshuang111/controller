package com.dongnaoedu.mall.controller;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dongnaoedu.mall.common.jedis.JedisClient;
import com.dongnaoedu.mall.pojo.TbThanks;
import com.dongnaoedu.mall.pojo.common.Result;
import com.dongnaoedu.mall.pojo.front.Order;
import com.dongnaoedu.mall.pojo.front.OrderInfo;
import com.dongnaoedu.mall.pojo.front.PageOrder;
import com.dongnaoedu.mall.service.OrderFrontService;
import com.dongnaoedu.mall.utils.ResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 */
@RestController
@Api( "订单")
public class OrderFrontController {

    private final static Logger log= LoggerFactory.getLogger(OrderFrontController.class);

    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private OrderFrontService orderService;

    @RequestMapping(value = "/member/orderList",method = RequestMethod.GET)
    @ApiOperation(value = "获得用户所有订单")
    public Result<PageOrder> getOrderList(String userId,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "5") int size){

        PageOrder pageOrder=orderService.getOrderList(Long.valueOf(userId), page, size);
        return new ResultUtil<PageOrder>().setData(pageOrder);
    }

    @RequestMapping(value = "/member/orderDetail",method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取订单")
    public Result<Order> getOrder(String orderId){

        Order order=orderService.getOrder(orderId);
        return new ResultUtil<Order>().setData(order);
    }

    @RequestMapping(value = "/member/addOrder",method = RequestMethod.POST)
    @ApiOperation(value = "创建订单")
    public Result<Object> addOrder(@RequestBody OrderInfo orderInfo, HttpServletRequest request){

        //防炸库验证
//        String ip= IPInfoUtil.getIpAddr(request);
//        if("0:0:0:0:0:0:0:1".equals(ip)){
//            ip="127.0.0.1";
//        }
//        String redisKey="addOrder_"+ip;
//        String temp=jedisClient.get(redisKey);
//        if(StringUtils.isNotBlank(temp)){
//            return new ResultUtil<Object>().setErrorMsg("您提交的太频繁啦，作者的学生服务器要炸啦！请2分钟后再试");
//        }

        String orderId=orderService.createOrder(orderInfo);

        //记录缓存
//        jedisClient.set(redisKey,"ADDED");
//        jedisClient.expire(redisKey,120);
        return new ResultUtil<Object>().setData(orderId.toString());
    }

    @RequestMapping(value = "/member/cancelOrder",method = RequestMethod.POST)
    @ApiOperation(value = "取消订单")
    public Result<Object> cancelOrder(@RequestBody Order order){

        int result=orderService.cancelOrder(order.getOrderId());
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/delOrder",method = RequestMethod.GET)
    @ApiOperation(value = "删除订单")
    public Result<Object> delOrder(String orderId){
        int result=orderService.delOrder(orderId);
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/payOrder",method = RequestMethod.POST)
    @ApiOperation(value = "支付订单")
    public Result<Object> payOrder(@RequestBody TbThanks tbThanks, HttpServletRequest request){

        //防炸库验证
//        String ip= IPInfoUtil.getIpAddr(request);
//        if("0:0:0:0:0:0:0:1".equals(ip)){
//            ip="127.0.0.1";
//        }
//        String redisKey="payOrder_"+ip;
//        String temp=jedisClient.get(redisKey);
//        if(StringUtils.isNotBlank(temp)){
//            return new ResultUtil<Object>().setErrorMsg("您提交的太频繁啦，作者的学生服务器要炸啦！请1分钟后再试");
//        }

        int result=orderService.payOrder(tbThanks);

        //记录缓存
//        jedisClient.set(redisKey,"ADDED");
//        jedisClient.expire(redisKey,60);
        return new ResultUtil<Object>().setData(result);
    }
}
