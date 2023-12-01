package com.makeFriends.dubbo.api;

import com.makeFriends.model.domain.User;

public interface UserApi {
    //根据手机号查询用户
    User findByMobile(String mobile);

    Long save(User user);

    void update(User user);

    User findByHuanxin(String huanxinId);

    User findById(Long id);
}
