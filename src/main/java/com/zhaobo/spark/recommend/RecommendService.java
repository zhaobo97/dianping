package com.zhaobo.spark.recommend;

import com.zhaobo.spark.mapper.RecommendDOMapper;
import com.zhaobo.spark.model.RecommendDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
@Service
public class RecommendService implements Serializable {
    @Autowired
    RecommendDOMapper recommendDOMapper;

    /**
     * 根据用户id召回shopList
     * @param userId 用户Id
     * @return
     */
    public List<Integer> recall(Integer userId){
        RecommendDO recommendDO = recommendDOMapper.selectByPrimaryKey(userId);
        String[] shopIdArr = recommendDO.getRecommend().split(",");
        List<Integer> shopIdList = new ArrayList<>();
        for (String shopIdAtr : shopIdArr) {
            shopIdList.add(Integer.valueOf(shopIdAtr));
        }
        return shopIdList;
    }
}
