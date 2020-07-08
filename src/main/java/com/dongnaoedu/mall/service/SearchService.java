package com.dongnaoedu.mall.service;


import com.dongnaoedu.mall.pojo.common.SearchResult;

public interface SearchService {

	SearchResult search(String keyword, int page, int size, String sort, int priceGt, int priceLte);
}
