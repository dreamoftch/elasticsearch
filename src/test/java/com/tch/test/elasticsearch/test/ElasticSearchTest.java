package com.tch.test.elasticsearch.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.sort.SortOrder;

import com.alibaba.fastjson.JSON;
import com.tch.test.elasticsearch.test.vo.NestedVO;
import com.tch.test.elasticsearch.test.vo.User;

public class ElasticSearchTest {

	public static final String INDEX = "test";

	public static final String TYPE = "organization";

	public static TransportClient buildClient() throws UnknownHostException {
		return TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
	}

	public static void closeClient(TransportClient client) {
		if(client != null) {
			client.close();
		}
	}

	/**
	 * 创建mapping（类似数据库的表结构）
	 * 
	 * @param client
	 *            es client
	 * @param index
	 *            索引名字
	 * @param type
	 *            索引下type的名字
	 * @throws Exception
	 */
	public static void createOrgMapping(TransportClient client) throws Exception {
		IndicesExistsResponse res = client.admin().indices().prepareExists(INDEX).execute().actionGet();
		if (res.isExists()) {
			DeleteIndexRequestBuilder delIdx = client.admin().indices().prepareDelete(INDEX);
			delIdx.execute().actionGet();
		}
		CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(INDEX);
		final XContentBuilder organizationBuilder = XContentFactory.jsonBuilder()
				.startObject()
				.startObject(TYPE)
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
				.startObject("industrialModeCodes")
	            .field("type", "integer")
	            .endObject()
				.startObject("updatedAt")
				.field("type", "date")
				.endObject()
				.endObject()
				.endObject()
				.endObject();
		createIndexRequestBuilder.addMapping(TYPE, organizationBuilder);
		// MAPPING DONE
		createIndexRequestBuilder.execute().actionGet();
	}

	/**
	 * 往索引中添加数据
	 * 
	 * @param client
	 */
	public static void index(TransportClient client, ESOrganization organization) {
		IndexResponse response = client.prepareIndex(INDEX, TYPE, String.valueOf(organization.getId()))
				.setSource(JSON.toJSONString(organization)).get();
		System.out.println(response.getId());
	}

	public static void getById(TransportClient client, String id) {
		GetResponse response = client.prepareGet(INDEX, TYPE, id).get();
		System.out.println(response.getSourceAsString());
	}

	public static void deleteById(TransportClient client, String id) {
		DeleteResponse response = client.prepareDelete(INDEX, TYPE, id).get();
		System.out.println(response);
	}

	public static void update(TransportClient client, ESOrganization organization)
			throws InterruptedException, ExecutionException {
		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, String.valueOf(organization.getId()))
				.doc(JSON.toJSONString(organization));
		UpdateResponse response = client.update(updateRequest).get();
		System.out.println(response);
	}

	public static void multiGet(TransportClient client, String... ids) {
		MultiGetResponse multiGetItemResponses = client.prepareMultiGet().add(INDEX, TYPE, ids).get();
		for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
			GetResponse response = itemResponse.getResponse();
			if (response.isExists()) {
				String json = response.getSourceAsString();
				System.out.println(json);
			}
		}
	}

	/**
	 * 复杂一点的搜索
	 * @param client
	 * @param searchReq
	 */
	public static void search(TransportClient client, OrgSearchReq searchReq) {
		BoolQueryBuilder orgQueryBuilder = QueryBuilders.boolQuery();
		if (searchReq.getKeyword() != null && searchReq.getKeyword().length > 0) {
			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
			for (String keyword : searchReq.getKeyword()) {
				queryBuilder.should(QueryBuilders.matchQuery("name", keyword));
			}
			orgQueryBuilder.must(queryBuilder);
		}
		if (!CollectionUtils.isEmpty(searchReq.getTypes())) {
			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
			for (Integer type : searchReq.getTypes()) {
				queryBuilder.should(QueryBuilders.matchQuery("type", type));
			}
			orgQueryBuilder.must(queryBuilder);
		}
		if (!CollectionUtils.isEmpty(searchReq.getIndustrialModeCodes())) {
	      BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
	      for (Integer industrialMode : searchReq.getIndustrialModeCodes()) {
	        queryBuilder.should(QueryBuilders.matchQuery("industrialModeCodes", industrialMode));
	      }
	      orgQueryBuilder.must(queryBuilder);
	    }
		if (searchReq.getUpdatedAt() != null && searchReq.getUpdatedAt().length > 0) {
	      if (searchReq.getUpdatedAt()[0] != null) {
	        orgQueryBuilder.must(QueryBuilders.rangeQuery("updatedAt").from(searchReq.getUpdatedAt()[0]));
	      }
	      if (searchReq.getUpdatedAt().length == 2 && searchReq.getUpdatedAt()[1] != null) {
	        orgQueryBuilder.must(QueryBuilders.rangeQuery("updatedAt").to(searchReq.getUpdatedAt()[1]));
	      }
	    }
		if (StringUtils.isEmpty(searchReq.getSortField())) {
			searchReq.setSortField("updatedAt");
		}
		System.out.println("orgQueryBuilder: " +orgQueryBuilder);

		SearchResponse searchResponse = client.prepareSearch(INDEX).setTypes(TYPE)
				.setQuery(orgQueryBuilder)
				.addSort(searchReq.getSortField(), getSortOrder(searchReq.getSortType()))
				.setFrom(searchReq.getOffset())
				.setSize(searchReq.getSize())
				.execute()
				.actionGet();
		List<ESOrganization> orgs = new ArrayList<>();
		for (SearchHit hit : searchResponse.getHits()) {
			ESOrganization org = JSON.parseObject(hit.getSourceAsString(), ESOrganization.class);
			orgs.add(org);
		}
		System.out.println(JSON.toJSONString(orgs, true));
	}

	private static SortOrder getSortOrder(String str) {
	    if (StringUtils.isEmpty(str)) {
	      return SortOrder.ASC;
	    }
	    for (SortOrder sortOrder : SortOrder.values()) {
	      if (sortOrder.name().equalsIgnoreCase(str)) {
	        return sortOrder;
	      }
	    }
	    return SortOrder.ASC;
	  }

	private static ESOrganization getUpdateOrg() {
		ESOrganization organization = new ESOrganization();
		organization.setId(1l);
		organization.setType(1111111111);
		return organization;
	}

	public static void addIndex(TransportClient client) {
		String[] orgNames = {
				"小米科技有限责任公司",
				"乐视网信息技术（北京）股份有限公司",
				"网易（杭州）网络有限公司",
				"阿里巴巴（中国）有限公司",
				"上海希格斯网络科技有限公司",
				"running man",
				"chicken run",
				"tom runs fast"
		};
		for (int i = 0; i< orgNames.length; i++) {
			ESOrganization organization = new ESOrganization();
			organization.setId(Long.valueOf(i+1));
			organization.setName(orgNames[i]);
			organization.setType(i * 1);
			organization.setIndustrialModeCodes(new Integer[]{1+i, 20+i, 30+i});
			organization.setUpdatedAt(new Date());
			index(client, organization);
		}
	}
	
	public static void max(TransportClient client) {
		MetricsAggregationBuilder<?> aggregation =
		        AggregationBuilders
		                .max("max_type")
		                .field("type");
		SearchResponse sr = client.prepareSearch().addAggregation(aggregation)
				.execute().actionGet();
		Max max_type = sr.getAggregations().get("max_type");
		double value = max_type.getValue();
		System.out.println("value: " + value);
	}
	
	public static void sum(TransportClient client) {
		MetricsAggregationBuilder<?> aggregation =
		        AggregationBuilders
		                .sum("sum_type")
		                .field("type");
		SearchResponse sr = client.prepareSearch().addAggregation(aggregation)
				.execute().actionGet();
		Sum sum_type = sr.getAggregations().get("sum_type");
		double value = sum_type.getValue();
		System.out.println("value: " + value);
	}
	
	public static void avg(TransportClient client) {
		MetricsAggregationBuilder<?> aggregation =
		        AggregationBuilders
		                .avg("avg_type")
		                .field("type");
		SearchResponse sr = client.prepareSearch().addAggregation(aggregation)
				.execute().actionGet();
		Avg avg_type = sr.getAggregations().get("avg_type");
		double value = avg_type.getValue();
		System.out.println("value: " + value);
	}
	
	public static void test(TransportClient client) {
		QueryBuilder queryBuilder = QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery("_all", "2"));
		System.out.println(queryBuilder);
		SearchResponse response = client.prepareSearch(INDEX).setTypes(TYPE).setQuery(queryBuilder)
			.execute().actionGet();
		System.out.println(response);
	}
	
	public static void main(String[] args) throws Exception {
		TransportClient client = null;
		try {
			client = buildClient();
			testNest(client);
//			test(client);
//			createOrgMapping(client);
//			addIndex(client);
//			update(client, getUpdateOrg());
//			getById(client, "1");
//			multiGet(client, "2", "3");
//			deleteById(client, "8");
//			//索引创建成功之后1秒钟左右，才会变得可用
//			TimeUnit.SECONDS.sleep(1);
//			search(client, getSearchReq());
//			max(client);
//			sum(client);
//			avg(client);
		} finally {
			closeClient(client);
		}
		
		
		
	}

	/**
	 * 测试nested object
	 * @param client
	 * @throws IOException
	 */
	private static void testNest(TransportClient client) throws IOException {			  
		buildNestedMapping(client);
		indexNested(client);
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		searchNested(client);
	}
	
	private static void searchNested(TransportClient client) {
		BoolQueryBuilder orgQueryBuilder = QueryBuilders.boolQuery();
//		orgQueryBuilder.must(QueryBuilders.matchQuery("group", "技术"));
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(QueryBuilders.matchQuery("users.name", "李四"));
		boolQueryBuilder.must(QueryBuilders.matchQuery("users.address", "上海"));
		orgQueryBuilder.must(QueryBuilders.nestedQuery("users", boolQueryBuilder));
		System.out.println("orgQueryBuilder: " +orgQueryBuilder);
		SearchResponse searchResponse = client.prepareSearch(INDEX).setTypes("test_type")
				.setQuery(orgQueryBuilder)
				.execute()
				.actionGet();
		List<NestedVO> orgs = new ArrayList<>();
		for (SearchHit hit : searchResponse.getHits()) {
			NestedVO org = JSON.parseObject(hit.getSourceAsString(), NestedVO.class);
			orgs.add(org);
		}
		System.out.println(JSON.toJSONString(orgs, true));
		
	}

	private static void indexNested(TransportClient client) {
		String[] groups = {"技术", "行政", "人事"};
		String[][] usernames = {{"张三", "张六"}, {"李四", "李七"}, {"王五", "王九"}};
		String[][] cities = {{"上海", "苏州"}, {"北京", "天津"}, {"深圳", "广州"}};
		for(int i = 1; i< 4; i++){
			NestedVO nestedVO = new NestedVO();
			nestedVO.setGroup(groups[i-1]);
			List<User> users = new ArrayList<>();
			for (int j = 0; j< 2; j++) {
				users.add(new User(usernames[i-1][j], cities[i-1][j]));
			}
			nestedVO.setUsers(users);
			IndexResponse response = client.prepareIndex(INDEX, "test_type", "" + i).setSource(JSON.toJSONString(nestedVO)).get();
			System.out.println(response.getId());
		}
	}

	private static void buildNestedMapping(TransportClient client) throws IOException {			  
		IndicesExistsResponse res = client.admin().indices().prepareExists(INDEX).execute().actionGet();
		if (res.isExists()) {
			DeleteIndexRequestBuilder delIdx = client.admin().indices().prepareDelete(INDEX);
			delIdx.execute().actionGet();
		}
		CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(INDEX);
		String type = "test_type";
//		final XContentBuilder organizationBuilder = XContentFactory.jsonBuilder()
//				.startObject()
//				.startObject(type)
//				.startObject("properties")
//				.endObject()
//				.endObject()
//				.endObject();
		final XContentBuilder organizationBuilder = XContentFactory.jsonBuilder()
				.startObject()
				.startObject(type)
				.startObject("properties")
				.startObject("group")
				.field("type", "string")
				.endObject()
				.startObject("users")
				.field("type", "nested")
				.startObject("properties")
				.startObject("name")
				.field("type", "string")
				.endObject()
				.startObject("address")
				.field("type", "string")
				.endObject()
				.endObject()
				.endObject()
				.endObject()
				.endObject()
				.endObject();
		createIndexRequestBuilder.addMapping(type, organizationBuilder);
		// MAPPING DONE
		createIndexRequestBuilder.execute().actionGet();
	}

	private static OrgSearchReq getSearchReq() {
		OrgSearchReq req = new OrgSearchReq();
		req.setKeyword(new String[]{"阿里巴巴", "乐视"});
		req.setTypes(Arrays.asList(3));
		req.setIndustrialModeCodes(Arrays.asList(23,33));
		req.setUpdatedAt(new Date[]{null, new Date()});
		return req;
	}

}
