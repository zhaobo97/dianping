package com.zhaobo.spark.recommend;

import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
@Service
public class RecommendSortService {
    private SparkSession spark;
    private LogisticRegressionModel lrModel;

    @PostConstruct
    public void init(){
        //加载LR模型
        spark = SparkSession.builder().master("local").appName("DianpingApp").getOrCreate();
        lrModel = LogisticRegressionModel.load("file:///D:\\dianpingtools\\devtool\\data\\lrmode");
    }

    public List<Integer> sort(List<Integer> shopIdList,Integer userId){
        //需要根据lrmode所需要11纬的x，生成特征，然后调用其预测方法
        List<ShopSortModel> list = new ArrayList<>();
        for(Integer shopId : shopIdList){
            //造的假数据，可以从数据库或缓存中拿到对应的性别，年龄，评分，价格等做特征转化生成feture向量
            Vector v = Vectors.dense(1,0,0,0,0,1,0.6,0,0,1,0);
            Vector result = lrModel.predictProbability(v);
            double[] arr = result.toArray();
            //arr[0] 表示正样本的概率， arr[1]表示正样本数据
            double score = arr[1];
            ShopSortModel shopSortModel = new ShopSortModel();
            shopSortModel.setShopId(shopId);
            shopSortModel.setScore(score);
            list.add(shopSortModel);
        }
        list.sort((o1, o2) -> Double.compare(o2.getScore(), o1.getScore()));
        return list.stream().map(shopSortModel -> shopSortModel.getShopId()).collect(Collectors.toList());
    }
}

