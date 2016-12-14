package com.tch.test.elasticsearch;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import com.alibaba.fastjson.JSON;
import com.tch.test.common.utils.CollectionUtil;
import com.tch.test.elasticsearch.utils.ElasticSearchUtil;
import com.tch.test.elasticsearch.vo.ESOrganization;
import com.tch.test.elasticsearch.vo.OrgSearchReq;

public class ElasticSearchTest {
	final static Logger logger = Logger.getLogger(ElasticSearchTest.class);
	
	private static String[] orgNames = {
			"小米科技有限责任公司",
			"乐视网信息技术（北京）股份有限公司",
			"网易（杭州）网络有限公司",
			"阿里巴巴（中国）有限公司",
			"上海希格斯网络科技有限公司",
			"running man",
			"chicken run",
			"tom runs fast",
			"Text-analyzer"
	};

	public static void main(String[] args) throws Exception {

		try {
			TransportClient client = ElasticSearchUtil.client();

//			prepareIndex(client);
			
//			updateOrg(client);
			
			test(client);
//			TimeUnit.SECONDS.sleep(1);
//			searchAllOrg(client);
			
			
//			 index(client);

			// search(client);

			// delete(client);

//			update(client);
			
//			multiGet(client);
			
//			bulk(client);
			
//			search(client);
			
//			fuzzySearch(client);
			
		} finally {
			ElasticSearchUtil.release();
		}
	}
	
	public void searchOrg(TransportClient client, OrgSearchReq searchReq) {
	    BoolQueryBuilder orgQueryBuilder = QueryBuilders.boolQuery();
	    if (searchReq.getOwnerId() != null) {
	      orgQueryBuilder.must(QueryBuilders.matchQuery("ownerId", searchReq.getOwnerId()));
	    }
	    if (searchReq.getKeyword() != null && searchReq.getKeyword().length > 0) {
	      BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
	      for (String keyword : searchReq.getKeyword()) {
	        queryBuilder.should(QueryBuilders.matchQuery("name", keyword));
	      }
	      orgQueryBuilder.must(queryBuilder);
	    }
	    if (!CollectionUtil.isEmpty(searchReq.getTypes())) {
	      BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
	      for (Integer type : searchReq.getTypes()) {
	        queryBuilder.should(QueryBuilders.matchQuery("type", type));
	      }
	      orgQueryBuilder.must(queryBuilder);
	    }
	    if (!CollectionUtil.isEmpty(searchReq.getLocationCodes())) {
	      BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
	      for (Integer locationCode : searchReq.getLocationCodes()) {
	        queryBuilder.should(QueryBuilders.matchQuery("allLocationCodes", locationCode));
	      }
	      orgQueryBuilder.must(queryBuilder);
	    }
	    if (!CollectionUtil.isEmpty(searchReq.getIndustryCodes())) {
	      BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
	      for (Integer industryCode : searchReq.getIndustryCodes()) {
	        queryBuilder.should(QueryBuilders.matchQuery("industryCodes", industryCode));
	      }
	      orgQueryBuilder.must(queryBuilder);
	    }
	    if (!CollectionUtil.isEmpty(searchReq.getNatureCodes())) {
	      BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
	      for (Integer natureCode : searchReq.getNatureCodes()) {
	        queryBuilder.should(QueryBuilders.matchQuery("natureCode", natureCode));
	      }
	      orgQueryBuilder.must(queryBuilder);
	    }
	    if (!CollectionUtil.isEmpty(searchReq.getIndustrialModeCodes())) {
	      BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
	      for (Integer industrialMode : searchReq.getIndustrialModeCodes()) {
	        queryBuilder.should(QueryBuilders.matchQuery("industrialModeCodes", industrialMode));
	      }
	      orgQueryBuilder.must(queryBuilder);
	    }
	    if (!CollectionUtil.isEmpty(searchReq.getEmpScales())) {
	      BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
	      for (Integer empScale : searchReq.getEmpScales()) {
	        queryBuilder.should(QueryBuilders.matchQuery("empScale", empScale));
	      }
	      orgQueryBuilder.must(queryBuilder);
	    }
	    if (!CollectionUtil.isEmpty(searchReq.getFinancingStatusList())) {
	      BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
	      for (Integer financingStatus : searchReq.getFinancingStatusList()) {
	        queryBuilder.should(QueryBuilders.matchQuery("financingStatus", financingStatus));
	      }
	      orgQueryBuilder.must(queryBuilder);
	    }
	    if (!CollectionUtil.isEmpty(searchReq.getHighlightCodes())) {
	      BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
	      for (Integer highlightCode : searchReq.getHighlightCodes()) {
	        queryBuilder.should(QueryBuilders.matchQuery("highlightCodes", highlightCode));
	      }
	      orgQueryBuilder.must(queryBuilder);
	    }
	    if (searchReq.getRegistedAt() != null && searchReq.getRegistedAt().length > 0) {
	      if (searchReq.getRegistedAt()[0] != null) {
	        orgQueryBuilder.must(QueryBuilders.rangeQuery("registedAt").from(searchReq.getRegistedAt()[0]));
	      }
	      if (searchReq.getRegistedAt().length == 2 && searchReq.getRegistedAt()[1] != null) {
	        orgQueryBuilder.must(QueryBuilders.rangeQuery("registedAt").to(searchReq.getRegistedAt()[1]));
	      }
	    }
	    if (searchReq.getUpdatedAt() != null && searchReq.getUpdatedAt().length > 0) {
	      if (searchReq.getUpdatedAt()[0] != null) {
	        orgQueryBuilder.must(QueryBuilders.rangeQuery("updatedAt").from(searchReq.getUpdatedAt()[0]));
	      }
	      if (searchReq.getUpdatedAt().length == 2 && searchReq.getUpdatedAt()[1] != null) {
	        orgQueryBuilder.must(QueryBuilders.rangeQuery("updatedAt").to(searchReq.getUpdatedAt()[1]));
	      }
	    }
	    if (searchReq.getSortField() == null || searchReq.getSortField().isEmpty()) {
	      searchReq.setSortField("updatedAt");
	    }

	    logger.info("orgQueryBuilder: " + orgQueryBuilder);

	    SearchResponse searchResponse = client.prepareSearch("tch")
	        .setTypes("organization")
	        .setQuery(orgQueryBuilder)
	        .addSort(searchReq.getSortField(), getSortOrder(searchReq.getSortType()))
//	        .setFrom(pagination.getOffset())
//	        .setSize(pagination.getSize())
	        .execute()
	        .actionGet();
	    List<ESOrganization> orgs = new ArrayList<>();
	    for (SearchHit hit : searchResponse.getHits()) {
	      System.out.println(hit.getSourceAsString());
	      ESOrganization org = JSON.parseObject(hit.getSourceAsString(), ESOrganization.class);
	      orgs.add(org);
	    }
//	    return pagination.setResults(orgs).setCount(searchResponse.getHits().getTotalHits());
	  }
	
	private SortOrder getSortOrder(String str) {
	    if (str == null || str.isEmpty()) {
	      return SortOrder.ASC;
	    }
	    for (SortOrder sortOrder : SortOrder.values()) {
	      if (sortOrder.name().equalsIgnoreCase(str)) {
	        return sortOrder;
	      }
	    }
	    return SortOrder.ASC;
	  }
	
	public static void prepareIndex(TransportClient client) throws Exception {
		IndicesExistsResponse res =  client.admin().indices().prepareExists("automind").execute().actionGet();
        if (res.isExists()) {
            DeleteIndexRequestBuilder delIdx = client.admin().indices().prepareDelete("automind");
            delIdx.execute().actionGet();
        }
        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate("automind");

        final XContentBuilder organizationBuilder = XContentFactory.jsonBuilder().startObject()
                .startObject("organization")
                .startObject("properties")
                .startObject("id")
                .field("type", "long")
                .endObject()
                .startObject("ownerId")
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
                .startObject("allLocationCodes")
                .field("type", "long")
                .endObject()
                .startObject("industryCodes")
                .field("type", "integer")
                .endObject()
                .startObject("natureCode")
                .field("type", "integer")
                .endObject()
                .startObject("industrialModeCodes")
                .field("type", "integer")
                .endObject()
                .startObject("empScale")
                .field("type", "integer")
                .endObject()
                .startObject("financingStatus")
                .field("type", "integer")
                .endObject()
                .startObject("highlightCodes")
                .field("type", "integer")
                .endObject()
                .startObject("registedAt")
                .field("type", "date")
                .endObject()
                .startObject("updatedAt")
                .field("type", "date")
                .endObject()
                .endObject()
                .endObject()
                .endObject();
        createIndexRequestBuilder.addMapping("organization", organizationBuilder);

        // MAPPING DONE
        createIndexRequestBuilder.execute().actionGet();
	}
	
	public static void test(TransportClient client) throws Exception {
		prepareIndex(client);
        
		for (int i = 0; i< orgNames.length; i++) {
			client.prepareIndex("automind", "organization", String.valueOf(i+1))
				.setSource(orgSource(i))
				.get();
//			System.out.println("indexResponse: " + indexResponse);
		}
	}
	
	public static String orgSource(int i) {
		ESOrganization organization = new ESOrganization();
		organization.setId(Long.valueOf(i+1));
		organization.setOwnerId(1L);
		organization.setName(orgNames[i]);
		organization.setType(i * 1);
		organization.setLocationCodes(new Long[]{Long.valueOf(1000*i), Long.valueOf(2000*i), Long.valueOf(3000*i)});
		organization.setIndustryCodes(new Integer[]{1*i, 2*i});
		organization.setNatureCode(5*i);
		organization.setIndustrialModeCodes(new Integer[]{1+i, 20+i, 30+i});
		organization.setEmpScale(6*i);
		organization.setFinancingStatus(3*i);
		organization.setHighlightCodes(new Integer[]{7+i,9+i});
		organization.setRegistedAt(new Date());
		organization.setUpdatedAt(new Date());
		String json = JSON.toJSONString(organization, true);
//		System.out.println(json);
		return json;
	}
	
	public static void searchAllOrg(TransportClient client){
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders
			.boolQuery();
//			.must(QueryBuilders.matchQuery("name", "公司"))
//			.must(QueryBuilders.matchQuery("location", 4000))
//			.must(QueryBuilders.matchQuery("location", 6000))
//			.must(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("location", 4000))
//					.should(QueryBuilders.matchQuery("location", 6000)));
//			.must(QueryBuilders.termQuery("natureCode", 10));
		System.out.println(boolQueryBuilder);
		
		SearchResponse searchResponse = client.prepareSearch("automind")
				.setTypes("organization")
				.setQuery(boolQueryBuilder)
				.addSort("natureCode", SortOrder.ASC)
				.addSort("registedAt", SortOrder.DESC)
//				.setFrom(1)
//				.setSize(2)
				.execute()
				.actionGet();
		System.out.println(searchResponse.getHits().getTotalHits());
		for (SearchHit hit : searchResponse.getHits()) {
			System.out.println(hit.getSourceAsString());
		}
	}
	
	public static void updateOrg(TransportClient client) throws Exception {
		UpdateRequest updateRequest = new UpdateRequest("automind", "organization", "2")
		        .doc(jsonBuilder()
		            .startObject()
		            	.array("location", 4000, 6000)
		                .field("natureCode", 10)
		            .endObject());
		UpdateResponse response = client.update(updateRequest).get();
		System.out.println("update response: " + response);
	}
	
	public static void deleteAllOrg(TransportClient client) {
		for (int i = 1; i< 6; i++) {
			DeleteResponse response = client.prepareDelete("automind", "organization", String.valueOf(i)).get();
			System.out.println("delete response: " + response);
		}
	}
	
	public static void fuzzySearch(TransportClient client) {
		SearchResponse response = client.prepareSearch("twitter")
		        .setTypes("tweet")
		        //模糊搜索
		        .setQuery(QueryBuilders.matchQuery("user", "kimchy").fuzziness(Fuzziness.AUTO))
		        //.setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
		        .setFrom(0).setSize(60).setExplain(true)
		        .execute()
		        .actionGet();
		System.out.println("fuzzySearch response: " + response);
		for(SearchHit searchHit : response.getHits()){
			System.out.println("fuzzySearch response: " + searchHit.getSourceAsString());
		}
		
	}
	
	public static void search(TransportClient client) {
		SearchResponse response = client.prepareSearch("twitter")
		        .setTypes("tweet")
//		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(QueryBuilders.termQuery("user", "kimchy1"))                 // Query
		        //.setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
		        .setFrom(0).setSize(60).setExplain(true)
		        .execute()
		        .actionGet();
		for(SearchHit searchHit : response.getHits()){
			System.out.println("search response: " + searchHit.getSourceAsString());
		}
		
	}

	public static void index(TransportClient client) throws Exception {
		IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
				.setSource(jsonBuilder().startObject()
						.field("user", "kimchy1")
						.field("postDate", new Date())
						.field("message", "trying out Elasticsearch")
						.array("tag", new String[]{"行业1", "行业2"})
						.endObject())
				.get();
		System.out.println("index response: " + response);
	}

	public static void get(TransportClient client) {
		GetResponse response = client.prepareGet("twitter", "tweet", "1").get();
		System.out.println("search response: " + response);
	}

	public static void delete(TransportClient client) {
		DeleteResponse response = client.prepareDelete("twitter", "tweet", "2").get();
		System.out.println("delete response: " + response);
	}

	public static void mergeUpdate(TransportClient client) throws Exception {
		UpdateRequest updateRequest = new UpdateRequest("twitter", "tweet", "1")
				.doc(jsonBuilder().startObject().field("gender", "male").endObject());
		client.update(updateRequest).get();
		UpdateResponse response = client.update(updateRequest).get();
		System.out.println("mergeUpdate response: " + response);
	}

	public static void update(TransportClient client) throws Exception {
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index("twitter");
		updateRequest.type("tweet");
		updateRequest.id("1");
		updateRequest.doc(jsonBuilder().startObject().field("gender", "female").endObject());
		UpdateResponse response = client.update(updateRequest).get();
		System.out.println("update response: " + response);
	}

	public static void multiGet(TransportClient client) {
		MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
				.add("twitter", "tweet", "1")
				.add("twitter", "tweet", "2", "3", "4")
				.add("another", "type", "foo")
				.get();

		for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
			GetResponse response = itemResponse.getResponse();
			if (response != null && response.isExists()) {
				String json = response.getSourceAsString();
				System.out.println("multiGet response: " + json);
			}
		}
	}
	
	public static void bulk(TransportClient client) throws Exception {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		// either use client#prepare, or use Requests# to directly build index/delete requests
		bulkRequest.add(client.prepareIndex("twitter", "tweet", "1")
		        .setSource(jsonBuilder()
		                    .startObject()
		                        .field("user", "kimchy")
		                        .field("message", "trying out Elasticsearch")
		                    .endObject()
		                  )
		        );

		bulkRequest.add(client.prepareIndex("twitter", "tweet", "2")
		        .setSource(jsonBuilder()
		                    .startObject()
		                        .field("user", "kimchy")
		                        .field("postDate", new Date())
		                        .field("message", "another post")
		                    .endObject()
		                  )
		        );

		BulkResponse bulkResponse = bulkRequest.get();
		if (bulkResponse.hasFailures()) {
			System.out.println("bulk response: " + bulkResponse);
		    // process failures by iterating through each bulk response item
		}
	}

}
