package com.dongnaoedu.mall.service;

import com.dongnaoedu.mall.pojo.dto.EsInfo;

/**
 * 商品搜索引擎服务
 */
public interface SearchItemService {

	/**
	 * 同步索引
	 * 
	 * @return
	 */
	int importAllItems();

	/**
	 * 同步单个商品索引
	 * 
	 * @param type
	 * @param itemId
	 * @return
	 */
	int refreshItem(int type, Long itemId);

	/**
	 * 获取ES基本信息
	 * 
	 * @return
	 */
	EsInfo getEsInfo();
}
