package com.makeFriends.dubbo.api;

import com.makeFriends.model.domain.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserInfoApi {
    public void save(UserInfo userInfo);
    public void update(UserInfo userInfo);
    UserInfo findById(Long userID);

    Map<Long, UserInfo> findByIds(List<Long> ids, UserInfo userInfo);
}
