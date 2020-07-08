package com.dongnaoedu.mall.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dongnaoedu.mall.common.exception.MallException;
import com.dongnaoedu.mall.pojo.common.SearchResult;
import com.dongnaoedu.mall.pojo.front.SearchItem;
import com.dongnaoedu.mall.service.SearchService;
import com.google.gson.Gson;

/**
 * 
 */
@Service
public class SearchServiceImpl implements SearchService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	TransportClient client;
	
	@Override
	public SearchResult search(String key, int page, int size, String sort, int priceGt, int priceLte) {
		try{
			SearchResult searchResult=new SearchResult();
			
			// 设置关键字匹配查询
			BoolQueryBuilder keyWordCondition = QueryBuilders.boolQuery();
			QueryBuilder matchName = matchQuery("productName", key);
			keyWordCondition.should(matchName);
			QueryBuilder subTitleName = matchQuery("subTitle", key);
			keyWordCondition.should(subTitleName);
			
			//设置分页
			if (page <= 0 ) { page = 1; }
			int start = (page - 1) * size;
			
			//设置高亮显示
			HighlightBuilder hiBuilder=new HighlightBuilder();
			hiBuilder.preTags("<a style=\"color: #e4393c\">");
			hiBuilder.postTags("</a>");
			hiBuilder.field("productName");
			hiBuilder.field("subTitle");

			SearchRequestBuilder searchRequest = client.prepareSearch("item").setTypes("itemList")
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					.setFrom(start).setSize(size)
					.setQuery(keyWordCondition).highlighter(hiBuilder)
					.setExplain(true);
			
			
			// 设置价格区间，这里用来做过滤条件，不需要记分，所以用filter过滤器
			if(priceGt >= 0 || priceLte >= 0) {
				RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("salePrice");
				if(priceGt >= 0) {
					rangeQuery.gt(priceGt);
				}
				if(priceLte >= 0) {
					rangeQuery.lt(priceLte);
				}
				searchRequest.setPostFilter(rangeQuery);
			}
			
			// 价格升降序显示
			if(! sort.isEmpty()) {
				SortOrder order = "-1".equals(sort) ? SortOrder.ASC : SortOrder.DESC;
				searchRequest.addSort("salePrice", order);
			}
			
			//执行搜索
			SearchResponse searchResponse = searchRequest.get();
			logger.debug(searchRequest.toString());
			
			SearchHits hits = searchResponse.getHits();
			// 返回总结果数
			searchResult.setRecordCount(hits.totalHits);
			List<SearchItem> list=new ArrayList<>();
			Gson gson = new Gson();
			if (hits.totalHits > 0) {
				// 计算总页数
				int totalPage = (int) (hits.totalHits + size - 1 / size);
				searchResult.setTotalPages(totalPage);
				
				for (SearchHit hit : hits) {
					SearchItem searchItem = gson.fromJson(hit.getSourceAsString(), SearchItem.class);
					//设置高亮字段
					HighlightField highlightName = hit.getHighlightFields().get("productName");
					if(highlightName != null && highlightName.getFragments() != null && highlightName.getFragments().length > 0) {
						String productName = highlightName.getFragments()[0].toString();
						searchItem.setProductName(productName);
					}
					HighlightField highlightTitle = hit.getHighlightFields().get("subTitle");
					if(highlightTitle != null && highlightTitle.getFragments() != null && highlightTitle.getFragments().length > 0) {
						String subTitle = highlightTitle.getFragments()[0].toString();
						searchItem.setSubTitle(subTitle);
					}
					//返回结果
					list.add(searchItem);
				}
			}
			searchResult.setItemList(list);

			return searchResult;
		}catch (Exception e){
			e.printStackTrace();
			throw new MallException("查询ES索引库出错");
		}
	}
}
