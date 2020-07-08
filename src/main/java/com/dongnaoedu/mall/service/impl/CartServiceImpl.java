package com.dongnaoedu.mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dongnaoedu.mall.common.jedis.JedisClient;
import com.dongnaoedu.mall.mapper.TbItemMapper;
import com.dongnaoedu.mall.pojo.TbItem;
import com.dongnaoedu.mall.pojo.front.CartProduct;
import com.dongnaoedu.mall.service.CartService;
import com.dongnaoedu.mall.utils.DtoUtil;
import com.google.gson.Gson;

@Service
public class CartServiceImpl implements CartService {

    private final static Logger log= LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private TbItemMapper itemMapper;
    
    @Autowired
    private JedisClient jedisClient;
    
    @Value("${CART_PRE}")
    private String CART_PRE;
    

    @Override
    public int addCart(long userId, long itemId, int num) {
    	
    	String userHashMapCarKey = CART_PRE+":"+userId;
    	String itemKey = itemId + "";
    	
    	// 判断购物车及商品是否存在
    	boolean cartExists = jedisClient.hexists(userHashMapCarKey, itemKey);
    	Gson gson = new Gson();
    	// 存在下，只需要增加商品的数量
    	if(cartExists) {
    		String jsonData = jedisClient.hget(userHashMapCarKey, itemKey);
    		if(null != jsonData) {
    			CartProduct cartProduct = gson.fromJson(jsonData, CartProduct.class);
    			cartProduct.setProductNum(cartProduct.getProductNum() + num);
    			jedisClient.hset(userHashMapCarKey, itemKey, gson.toJson(cartProduct));
    			return 1;
    		}else {
    			return 0;
    		}
    	}
        //如果不存在，根据商品id取商品信息
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        if(item==null){
            return 0;
        }
        CartProduct cartProduct= DtoUtil.TbItem2CartProduct(item);
        cartProduct.setProductNum((long) num);
        cartProduct.setChecked("1");
        jedisClient.hset(userHashMapCarKey, itemKey, gson.toJson(cartProduct));
        return 1;
    }
    
    /*
     * 一次获取购物车商品内容
     */
    @Override
    public List<CartProduct> getCartList(long userId) {
    	List<CartProduct> list = new ArrayList<>();
    	String userHashMapCarKey = CART_PRE+":"+userId;
    	List<String> jsonList = jedisClient.hvals(userHashMapCarKey);
    	Gson gson = new Gson();
    	for(String json : jsonList) {
    		CartProduct cartItem = gson.fromJson(json, CartProduct.class);
    		list.add(cartItem);
    	}
        return list;
    }
    
    /*
     * 更新购物车商品数量信息
     */
    @Override
    public int updateCartNum(long userId, long itemId, int num, String checked) {
    	String userHashMapCarKey = CART_PRE+":"+userId;
    	String itemKey = itemId + "";
    	String jsonData = jedisClient.hget(userHashMapCarKey, itemKey);
		if(null == jsonData) {
			return 0;
		}
		Gson gson = new Gson();
		CartProduct cartProduct = gson.fromJson(jsonData, CartProduct.class);
		cartProduct.setProductNum((long) num);
		cartProduct.setChecked(checked);
		jedisClient.hset(userHashMapCarKey, itemKey, gson.toJson(cartProduct));
        return 1;
    }
    
    /*
     * 选中所有的商品
     */
    @Override
    public int checkAll(long userId,String checked) {
    	String userHashMapCarKey = CART_PRE+":"+userId;
    	List<String> jsonList = jedisClient.hvals(userHashMapCarKey);
    	Gson gson = new Gson();
    	for(String json : jsonList) {
    		CartProduct cartItem = gson.fromJson(json, CartProduct.class);
    		if("true".equals(checked)) {
    			cartItem.setChecked("1");
    		}else {
    			cartItem.setChecked("0");
    		}
    		String itemKey = cartItem.getProductId()+"";
    		jedisClient.hset(userHashMapCarKey, itemKey, gson.toJson(cartItem));
    	}
        return 1;
    }
    
    /*
     * 删除选中的商品
     */
    @Override
    public int delChecked(long userId) {
    	String userHashMapCarKey = CART_PRE+":"+userId;
    	List<String> jsonList = jedisClient.hvals(userHashMapCarKey);
    	Gson gson = new Gson();
    	for(String json : jsonList) {
    		CartProduct cartItem = gson.fromJson(json, CartProduct.class);
    		String itemKey = cartItem.getProductId()+"";
    		if("1".equals(cartItem.getChecked())) {
    			jedisClient.hdel(userHashMapCarKey, itemKey);
    		}
    	}
        return 1;
    }
    
    /*
     * 删除购物车中的某个商品
     */
    @Override
    public int deleteCartItem(long userId, long itemId) {
    	String userHashMapCarKey = CART_PRE+":"+userId;
    	String itemKey = itemId+"";
    	jedisClient.hdel(userHashMapCarKey, itemKey);
        return 1;
    }

}
