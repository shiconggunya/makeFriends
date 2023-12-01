package com.makeFriends.dubbo.api;

import com.makeFriends.model.mongdb.Friend;

import java.util.List;

public interface FriendApi {

     void save(Long userId, Long friendId);

    List<Friend> findByUserId(Long userId, Integer page, Integer pagesize);
}
