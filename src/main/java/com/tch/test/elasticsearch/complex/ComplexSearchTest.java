package com.tch.test.elasticsearch.complex;

import com.alibaba.fastjson.JSON;
import com.tch.test.elasticsearch.utils.ElasticSearchUtil;
import com.tch.test.elasticsearch.vo.ESObject;
import com.tch.test.elasticsearch.vo.User;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by tianch on 2017/1/19.
 */
public class ComplexSearchTest {

  private static final String INDEX = "tianch";
  private static final String MAPPING = "es_object";
  private static TransportClient client = null;

  static {
    client = ElasticSearchUtil.client();
  }

  public static void main(String[] args) throws Exception {
    try {
      prepareIndex();
      insertData();
      Thread.sleep(2000);
      search();
    } finally {
      client.close();
    }
  }

  private static void search() {
    BoolQueryBuilder query = QueryBuilders.boolQuery();
    BoolQueryBuilder studentsQuery = QueryBuilders.boolQuery()
        .must(QueryBuilders.termQuery("students.name", "student-20"))
        .must(QueryBuilders.termQuery("students.id", 2));
    query.must(QueryBuilders.nestedQuery("students", studentsQuery));

    BoolQueryBuilder teacherQuery = QueryBuilders.boolQuery()
        .must(QueryBuilders.termQuery("teacher.id", 2))
        .must(QueryBuilders.termQuery("teacher.name", "teacher-2"));
    query.must(QueryBuilders.nestedQuery("teacher", teacherQuery));

    SearchResponse response = client.prepareSearch(INDEX)
        .setTypes(MAPPING)
        .setQuery(query)
        .setFrom(0).setSize(60).setExplain(true)
        .execute()
        .actionGet();
    for(SearchHit searchHit : response.getHits()){
      System.out.println("search response: " + searchHit.getSourceAsString());
    }
  }

  private static void insertData() {
    for (int i = 1; i< 6; i++) {
      ESObject esObject = new ESObject();
      esObject.setId(Long.valueOf(i));
      esObject.setRefIds(Arrays.asList(Long.valueOf(i * 10), Long.valueOf(i * 10 + 1), Long.valueOf(i * 10 + 2)));
      esObject.setTeacher(new User(Long.valueOf(i), "teacher-" + i));
      esObject.setStudents(Arrays.asList(
          new User(Long.valueOf(i), "student-" + i * 10),
          new User(Long.valueOf(i), "student-" + i * 10 + 1),
          new User(Long.valueOf(i), "student-" + i * 10 + 2)));
      client.prepareIndex(INDEX, MAPPING, String.valueOf(esObject.getId()))
          .setSource(JSON.toJSONString(esObject)).get();
    }
  }

  public static void prepareIndex() throws Exception {
    IndicesExistsResponse res =  client.admin().indices().prepareExists(INDEX).execute().actionGet();
    if (res.isExists()) {
      DeleteIndexRequestBuilder delIdx = client.admin().indices().prepareDelete(INDEX);
      delIdx.execute().actionGet();
    }
    CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(INDEX);
    createIndexRequestBuilder.addMapping(MAPPING, getMapping());
    // MAPPING DONE
    createIndexRequestBuilder.execute().actionGet();
  }

  private static String getMapping() throws Exception {
    return new String(Files.readAllBytes(Paths.get(ComplexSearchTest.class.getClassLoader()
        .getResource("ESObject.json").toURI())));
  }

}
