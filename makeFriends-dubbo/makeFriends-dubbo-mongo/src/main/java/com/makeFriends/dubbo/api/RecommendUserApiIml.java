package com.makeFriends.dubbo.api;

import com.makeFriends.model.mongdb.RecommendUser;
import com.makeFriends.model.vo.PageResult;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@DubboService
public class RecommendUserApiIml implements RecommendUserApi {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public RecommendUser queryWithMaxScore(Long toUserId) {
        //根据toUserId查询，根据评分score排序，获取第一条
        //构建Criteria
        Criteria criteria = Criteria.where("toUserId").is(toUserId);

        //构建Query对象
        Query query = Query.query(criteria).with(Sort.by(Sort.Order.desc("score")))
                .limit(1);

        //调用mongoTemplate查询
        RecommendUser one = mongoTemplate.findOne(query, RecommendUser.class);
        return one;
    }


    //分页查询
    public PageResult queryRecommendUserList(Integer page, Integer pagesize, Long toUserId) {
        //1、构建Criteria对象
        Criteria criteria = Criteria.where("toUserId").is(toUserId);
        //2、创建Query对象
        Query query = Query.query(criteria).with(Sort.by(Sort.Order.desc("score"))).limit(pagesize)
                .skip((page - 1) * pagesize);
        //3、调用mongoTemplate查询
        List<RecommendUser> list = mongoTemplate.find(query, RecommendUser.class);
        long count = mongoTemplate.count(query, RecommendUser.class);
        //4、构建返回值PageResult
        return  new PageResult(page,pagesize, (int) count,list);
    }

    @Override
    public RecommendUser queryByUserId(Long userId, Long toUserId) {
        Criteria criteria = Criteria.where("toUserId").is(toUserId).and("userId").is(userId);
        Query query = Query.query(criteria);
        RecommendUser user = mongoTemplate.findOne(query, RecommendUser.class);
        if(user == null) {
            user = new RecommendUser();
            user.setUserId(userId);
            user.setToUserId(toUserId);
            //构建缘分值
            user.setScore(95d);
        }
        return user;
    }
}
