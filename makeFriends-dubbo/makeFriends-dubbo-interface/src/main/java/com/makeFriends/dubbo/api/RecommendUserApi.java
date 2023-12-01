package com.makeFriends.dubbo.api;

import com.makeFriends.model.mongdb.RecommendUser;
import com.makeFriends.model.vo.PageResult;

public interface RecommendUserApi {
    RecommendUser queryWithMaxScore(Long toUserId);

    PageResult queryRecommendUserList(Integer page, Integer pagesize, Long userId);

    RecommendUser queryByUserId(Long userId, Long userId1);
}
