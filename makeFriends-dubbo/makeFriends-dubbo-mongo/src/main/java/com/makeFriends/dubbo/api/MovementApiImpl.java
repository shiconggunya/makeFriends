package com.makeFriends.dubbo.api;



import cn.hutool.core.collection.CollUtil;
import com.makeFriends.dubbo.utils.IdWorker;
import com.makeFriends.dubbo.utils.TimeLineService;
import com.makeFriends.model.mongdb.Movement;
import com.makeFriends.model.mongdb.MovementTimeLine;
import com.makeFriends.model.vo.PageResult;
import org.apache.dubbo.config.annotation.DubboService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;


@DubboService
public class MovementApiImpl implements MovementApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TimeLineService timeLineService;

    //发布动态
    public void publish(Movement movement) {
        //1、保存动态详情
        try {
            //设置PID
            movement.setPid(idWorker.getNextId("movement"));
            //设置时间
            movement.setCreated(System.currentTimeMillis());
            //movement.setId(ObjectId.get());
            mongoTemplate.save(movement);
            timeLineService.saveTimeLine(movement.getUserId(),movement.getId());
        } catch (Exception e) {
            //忽略事务处理
            e.printStackTrace();
        }
    }

    @Override
    public PageResult findByUserId(Long userId, Integer page, Integer pagesize) {
        Criteria criteria = Criteria.where("userId").is(userId);
        Query query = Query.query(criteria).skip((page -1 ) * pagesize).limit(pagesize)
                .with(Sort.by(Sort.Order.desc("created")));
        List<Movement> movements = mongoTemplate.find(query, Movement.class);
        return new PageResult(page,pagesize,0,movements);
    }

    @Override
    public List<Movement> findFriendMovements(Long friendId, Integer page, Integer pagesize) {
        //1、查询好友时间线表
        Query query = Query.query(Criteria.where("friendId").in(friendId))
                .skip((page - 1)*pagesize).limit(pagesize)
                .with(Sort.by(Sort.Order.desc("created")));
        List<MovementTimeLine> lines = mongoTemplate.find(query, MovementTimeLine.class);
        //2、提取动态id集合
        List<ObjectId> movementIds = CollUtil.getFieldValues(lines, "movementId", ObjectId.class);
        //3、根据动态id查询动态详情
        Query movementQuery = Query.query(Criteria.where("id").in(movementIds));
        List<Movement> movements = mongoTemplate.find(movementQuery, Movement.class);
        return movements;
    }

    //随机获取
    public List<Movement> randomMovements(Integer counts) {

        TypedAggregation aggregation = Aggregation.newAggregation(Movement.class,
                Aggregation.sample(counts));

        AggregationResults<Movement> movements = mongoTemplate.aggregate(aggregation,Movement.class);

        return movements.getMappedResults();
    }


    //根据PID查询
    public List<Movement> findByPids(List<Long> pids) {
        Query query = Query.query(Criteria.where("pId").in(pids));
        return mongoTemplate.find(query, Movement.class);
    }

    @Override
    public Movement findById(String movementId) {
        return mongoTemplate.findById(movementId,Movement.class);
    }

}