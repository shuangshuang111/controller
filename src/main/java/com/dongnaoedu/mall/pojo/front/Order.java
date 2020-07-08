package com.dongnaoedu.mall.pojo.front;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.dongnaoedu.mall.pojo.TbAddress;

public class Order implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String orderId;

    private BigDecimal orderTotal;

    private TbAddress addressInfo;

    private List<CartProduct> goodsList;

    private String orderStatus;

    private String createDate;

    private String closeDate;

    private String finishDate;

    private String payDate;

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public TbAddress getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(TbAddress addressInfo) {
        this.addressInfo = addressInfo;
    }

    public List<CartProduct> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<CartProduct> goodsList) {
        this.goodsList = goodsList;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
