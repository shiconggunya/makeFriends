package com.makeFriends.dubbo.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.makeFriends.model.domain.UserInfo;

public interface BlackListApi {
    IPage<UserInfo> findByUserId(Long userId, int page, int size);

    void delete(Long userId, Long blackUserId);
}
