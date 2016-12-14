package com.tch.test.elasticsearch;

import java.io.IOException;

import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.tch.test.elasticsearch.utils.ElasticSearchUtil;

/**
 * 学习ES各种query
 * @author higgs
 *
 */
public class ElasticSearchQueryTest {

	public static void main(String[] args) throws IOException {
//		boolQuery();
//		multiMatchQuery();
//		minShouldMatch();
//		combinFilters();
//		t1();
		BoolQueryBuilder builder = QueryBuilders.boolQuery()
			.must(QueryBuilders.matchQuery("title", "search"))
			.must(QueryBuilders.matchQuery("content", "Elasticsearch"))
			.filter(QueryBuilders.termQuery("status", "published"))
			.filter(QueryBuilders.rangeQuery("publish_date").gte("2015-01-01"));
		System.out.println(builder);
	}
	
	public static void t1() throws IOException {
		final XContentBuilder organizationBuilder = XContentFactory.jsonBuilder().startObject()
                .startObject("organization")
                .startObject("properties")
                .startObject("id")
                .field("type", "long")
                .endObject()
                .startObject("name")
                .field("type", "string")
                .endObject()
                .startObject("type")
                .field("type", "integer")
                .endObject()
                .startObject("locationCodes")
                .field("type", "long")
                .endObject()
                .startObject("updatedAt")
                .field("type", "date")
                .endObject()
                .endObject()
                .endObject()
                .endObject();
        System.out.println(organizationBuilder.string());
	}
	
	public static void combinFilters() {
		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.should(QueryBuilders.matchQuery("industryCodes", 4))
				.should(QueryBuilders.matchQuery("type", 4))
				//设置最少需要满足的should条件的个数
				.minimumNumberShouldMatch(1)
				.filter(QueryBuilders.matchQuery("financingStatus", 12));
		SearchResponse response = ElasticSearchUtil.client()
				.prepareSearch("automind")
				.setTypes("organization")
				.setQuery(queryBuilder)
				.execute()
				.actionGet();
		System.out.println(response);
	}
	
	public static void minShouldMatch() {
		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.should(QueryBuilders.matchQuery("industryCodes", 4))
				.should(QueryBuilders.matchQuery("type", 4))
				//设置最少需要满足的should条件的个数
				.minimumNumberShouldMatch(2);
		SearchResponse response = ElasticSearchUtil.client()
				.prepareSearch("automind")
				.setTypes("organization")
				.setQuery(queryBuilder)
				.execute()
				.actionGet();
		System.out.println(response);
	}
	
	/**
	 * 在不同的字段上面使用相同的搜索条件
	 * https://www.elastic.co/guide/en/elasticsearch/guide/current/_cross_fields_entity_search.html
	 */
	public static void multiMatchQuery() {
		//在"street", "city", "country", "postcode"四个字段上面都搜索"Poland Street W1V"
//		{
//		  "query": {
//		    "multi_match": {
//		      "query":       "Poland Street W1V",
//		      "type":        "most_fields",
//		      "fields":      [ "street", "city", "country", "postcode" ]
//		    }
//		  }
//		}
		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(4, "industrialModeCodes", "industryCodes")
				.type(Type.MOST_FIELDS);
		SearchResponse response = ElasticSearchUtil.client()
				.prepareSearch("automind")
				.setTypes("organization")
				.setQuery(queryBuilder)
				.execute()
				.actionGet();
		System.out.println(response);
	}
	
	public static void boolQuery() {
		SearchResponse response = ElasticSearchUtil.client()
				.prepareSearch("automind")
				.setTypes("organization")
				.setQuery(QueryBuilders.matchQuery("name", "Text-analyzer"))
//				.setExplain(true)
				.execute()
				.actionGet();
		System.out.println(response);
	}
	
}
