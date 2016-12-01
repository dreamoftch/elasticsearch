package com.tch.test.elasticsearch;

import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * 学习ES各种query
 * @author higgs
 *
 */
public class ElasticSearchQueryTest {

	public static void main(String[] args) {
		boolQuery();
	}
	
	public static void boolQuery() {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
			.should(QueryBuilders.matchQuery("username", "zhangsan"))
			.should(QueryBuilders.matchQuery("address", "shanghai"));
		System.out.println(boolQueryBuilder);
	}
	
	
	
}
