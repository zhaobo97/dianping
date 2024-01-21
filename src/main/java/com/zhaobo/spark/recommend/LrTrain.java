package com.zhaobo.spark.recommend;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.io.IOException;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
public class LrTrain {
    public static void main(String[] args) throws IOException {
        //初始化spark运行环境
        SparkSession spark = SparkSession.builder().master("local").appName("SparkApp").getOrCreate();
        JavaRDD<String> csvFile = spark.read()
                .textFile("file:///Users/mac/Desktop/data/feature.csv").toJavaRDD();
        JavaRDD<Row> ratingJavaRDD = csvFile.map(new Function<String, Row>() {
            @Override
            public Row call(String v1) throws Exception {
                v1 = v1.replace("\"","");
                String[] strArr = v1.split(",");
                return RowFactory.create(Double.parseDouble(strArr[11]),
                        Vectors.dense(Double.parseDouble(strArr[0]),
                        Double.parseDouble(strArr[1]),
                        Double.parseDouble(strArr[2]),
                        Double.parseDouble(strArr[3]),
                        Double.parseDouble(strArr[4]),
                        Double.parseDouble(strArr[5]),
                        Double.parseDouble(strArr[6]),
                        Double.parseDouble(strArr[7]),
                        Double.parseDouble(strArr[8]),
                        Double.parseDouble(strArr[9]),
                        Double.parseDouble(strArr[10])));
            }
        });
        StructType schema = new StructType(
            new StructField[]{
                    new StructField("label", DataTypes.DoubleType,false, Metadata.empty()),
                     new StructField("features",new VectorUDT(),false, Metadata.empty())
            }
        );
        // Dataset中只有2列，第二列是Vector向量
        Dataset<Row> data = spark.createDataFrame(ratingJavaRDD, schema);
        Dataset<Row>[] dataArr = data.randomSplit(new double[]{0.8,0.2});
        Dataset<Row> trainData = dataArr[0];
        Dataset<Row> testData = dataArr[1];
        // elasticNetParam弹性网络参数，setFamily设置为多分类，防止过拟合
        LogisticRegression lr = new LogisticRegression().setMaxIter(10).setRegParam(0.3)
                        .setElasticNetParam(0.8).setFamily("multinomial");
        // 模型训练
        LogisticRegressionModel lrModel = lr.fit(trainData);
        // 保存训练模型model
        lrModel.save("file:///Users/mac/Desktop/data/lrModel");
        // 用模型预测
        Dataset<Row> predictions = lrModel.transform(testData);
        // 根据预测数据评价模型的准确度
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator();
        double accuracy = evaluator.setMetricName("accuracy").evaluate(predictions);

        System.out.println("auc="+accuracy);
    }
}
