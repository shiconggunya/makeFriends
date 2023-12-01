package com.makeFriends.dubbo.api;

import com.makeFriends.model.mongdb.Movement;
import com.makeFriends.model.vo.PageResult;

import java.util.List;

public interface MovementApi {
    void publish(Movement movement);

    PageResult findByUserId(Long userId, Integer page, Integer pagesize);

    List<Movement> findFriendMovements(Long userId, Integer page, Integer pagesize);

    List<Movement> randomMovements(Integer pagesize);

    List<Movement> findByPids(List<Long> pids);

    Movement findById(String movementId);
}
