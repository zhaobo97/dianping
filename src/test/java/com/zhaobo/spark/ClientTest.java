package com.zhaobo.spark;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@SpringBootTest
public class ClientTest {

    @Resource
    private RestHighLevelClient client;

    // 最简单的查询
    @Test
    public void test01() throws IOException {

        SearchRequest request = new SearchRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery("name", "凯悦"));
        builder.timeout(TimeValue.MINUS_ONE);
        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            String id = hit.getSourceAsMap().get("id").toString();
            log.info("shop id : {}",id);
        }
    }

    // 用restful request body查询
    @Test
    public void test02() throws IOException {
        String keyword = "凯悦";
        BigDecimal latitude = new BigDecimal("31.2489762");
        BigDecimal longitude = new BigDecimal("121.652763");

        Request request = new Request("GET", "/shop/_search");
        String reqJson = "{\n" +
                "  \"_source\": \"*\",\n" +
                "  \"script_fields\": {\n" +
                "    \"distance\": {\n" +
                "      \"script\": {\n" +
                "        \"source\": \"haversin(lat,lon,doc['location'].lat,doc['location'].lon)\",\n" +
                "        \"lang\": \"expression\"\n" +
                "        , \"params\": {\"lat\":"+latitude.toString()+",\"lon\":"+longitude.toString()+"}\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"query\":{\n" +
                "    \"function_score\": {\n" +
                "      \"query\": {\n" +
                "        \"bool\": {\n" +
                "          \"must\": [\n" +
                "            {\"match\": {\"name\":{\"query\":\""+keyword+"\"}}},\n" +
                "            {\"term\": {\"seller_disabled_flag\":0}}\n" +
                "          ]\n" +
                "        }\n" +
                "      },\n" +
                "      \"functions\": [\n" +
                "        {\n" +
                "          \"gauss\": {\n" +
                "            \"location\": {\n" +
                "              \"origin\": \""+latitude.toString()+","+longitude.toString()+"\",\n" +
                "              \"scale\": \"100km\",\n" +
                "              \"offset\": \"0km\",\n" +
                "              \"decay\": 0.5\n" +
                "            }\n" +
                "          },\n" +
                "          \"weight\": 9\n" +
                "        },\n" +
                "        {\n" +
                "          \"field_value_factor\": {\n" +
                "            \"field\": \"remark_score\"\n" +
                "          },\n" +
                "          \"weight\": 0.2\n" +
                "        },\n" +
                "        {\"field_value_factor\": {\n" +
                "          \"field\": \"seller_remark_score\"\n" +
                "        },\n" +
                "        \"weight\": 0.2\n" +
                "        }\n" +
                "      ],\n" +
                "      \"boost_mode\": \"sum\",\n" +
                "      \"score_mode\": \"sum\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"sort\": [\n" +
                "    {\n" +
                "      \"_score\": {\n" +
                "        \"order\": \"desc\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        log.info(reqJson);
        request.setJsonEntity(reqJson);
        Response response = client.getLowLevelClient().performRequest(request);
        HttpEntity entity = response.getEntity();
        String jsonResponse = EntityUtils.toString(entity);
        JSONObject jsonObject = JSON.parseObject(jsonResponse);
        JSONArray jsonArray = jsonObject.getJSONObject("hits").getJSONArray("hits");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            String id = jsonObject1.get("_id").toString();
            String distance = jsonObject1.getJSONObject("fields").getJSONArray("distance").get(0).toString();
            BigDecimal distanceNum = new BigDecimal(distance);
        }
    }
}
