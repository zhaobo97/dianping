package com.zhaobo.spark.recommend;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.io.Serializable;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
public class AlsRecallTrain implements Serializable {

    public static void main(String[] args) throws IOException {
        //初始化spark运行环境
        SparkSession spark = SparkSession.builder().master("local").appName("SparkApp").getOrCreate();
        JavaRDD<String> csvFile = spark.read()
                .textFile("file:///Users/mac/Desktop/data/behavior.csv").toJavaRDD();
        JavaRDD<Rating> ratingJavaRDD = csvFile.map(new Function<String, Rating>() {
            @Override
            public Rating call(String str) throws Exception {
                return Rating.parseRating(str);
            }
        });
        Dataset<Row> rating = spark.createDataFrame(ratingJavaRDD,Rating.class);
        //将所有的rating数据分成82开
        Dataset<Row>[] ratings = rating.randomSplit(new double[]{0.8,0.2});
        Dataset<Row> trainingData = ratings[0];
        Dataset<Row> predictData = ratings[1];
        ALS als = new ALS().setMaxIter(10).setRank(5).setRegParam(0.01).
                setUserCol("userId").setItemCol("shopId").setRatingCol("rating");
        //模型训练
        ALSModel alsModel = als.fit(trainingData);
        //模型评测
        Dataset<Row> predictions = alsModel.transform(predictData);
        //rmse 均方差根误差，预测值与真实值之间误差的平方和除以观测次数，开个根号
        RegressionEvaluator evaluator = new RegressionEvaluator().setMetricName("rmse")
                .setLabelCol("rating").setPredictionCol("prediction");
        double rmse = evaluator.evaluate(predictions);
        System.out.println("rmse = "+rmse);
        //保存数据模型
        alsModel.save("file:///Users/mac/Desktop/data/alsModel");
    }

    @Getter
    @Setter
    public static class Rating implements Serializable{
        private int userId;
        private int shopId;
        private int rating;
        //处理csv数据
        public static Rating parseRating(String str){
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