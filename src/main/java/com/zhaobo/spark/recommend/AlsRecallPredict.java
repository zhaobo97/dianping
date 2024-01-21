package com.zhaobo.spark.recommend;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
public class AlsRecallPredict {
    public static void main(String[] args) throws SQLException {
        //初始化spark运行环境
        SparkSession spark = SparkSession.builder().master("local").appName("SparkApp").getOrCreate();
        //加载模型进内存
        ALSModel alsModel = ALSModel.load("file:///Users/mac/Desktop/data/alsModel");

        JavaRDD<String> csvFile = spark.read()
                .textFile("file:///Users/mac/Desktop/data/behavior.csv").toJavaRDD();
        JavaRDD<Rating> ratingJavaRDD = csvFile.map(new Function<String, Rating>() {
            @Override
            public Rating call(String str) throws Exception {
                return Rating.parseRating(str);
            }
        });
        Dataset<Row> rating = spark.createDataFrame(ratingJavaRDD, Rating.class);

        //给5个user做召回结果的预测，给5个用户离线推荐20个商品
        Dataset<Row> users = rating.select(alsModel.getItemCol()).distinct().limit(5);
        Dataset<Row> usersRecs = alsModel.recommendForItemSubset(users, 20);
        //以分片的方式处理，对每个分片遍历
        usersRecs.foreachPartition(new ForeachPartitionFunction<Row>() {

            @Override
            public void call(Iterator<Row> t) throws Exception {
                //建立数据库连接
                String insertSql = "insert into recommend(id,recommend) values (?,?)";
                Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/spark_db?user=root&password=root123456&useUnicode=true&useSSL=false&characterEncoding=UTF-8");
                PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
                // N名用户的粗排shop推荐（前20条）
                List<Map<String, Object>> data = new ArrayList<>();
                // 对分片内剩余的的row遍历，分片中可能是3行记录也可能是2行
                t.forEachRemaining(action -> {
                    int userId = action.getInt(0);
                    // 推荐的shop id列表，每个shop id 有对应的打分，并且已经倒序排序
                    List<GenericRowWithSchema> recommendationList = action.getList(1);
                    List<Integer> shopIdList = new ArrayList<>();
                    recommendationList.forEach(row -> {
                        Integer shopId = row.getInt(0);
                        shopIdList.add(shopId);
                    });
                    // 将推荐的shopIdList展开成字符串
                    String recommend = StringUtils.join(shopIdList, ",");
                    Map<String, Object> map = new HashMap<>(2);
                    map.put("userId", userId);
                    map.put("recommend", recommend);
                    data.add(map);
                });
                data.forEach(recommends -> {
                    try {
                        preparedStatement.setInt(1, Integer.parseInt(recommends.get("userId").toString()));
                        preparedStatement.setString(2, recommends.get("recommend").toString());
                        preparedStatement.addBatch();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                preparedStatement.executeBatch();
                connection.close();
            }
        });

    }

    @Getter
    @Setter
    public static class Rating implements Serializable {

        private static final long serialVersionUID = 1L;
        private int userId;
        private int shopId;
        private int rating;

        public static Rating parseRating(String str) {
            Rating rating = new Rating();
            str = str.replaceAll("\"", "");
            String[] strArr = str.split(",");
            rating.setUserId(Integer.parseInt(strArr[0]));
            rating.setShopId(Integer.parseInt(strArr[1]));
            rating.setRating(Integer.parseInt(strArr[2]));
            return rating;
        }
    }
}
