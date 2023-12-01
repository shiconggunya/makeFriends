package com.makeFriends.dubbo.api;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.makeFriends.dubbo.mapper.UserInfoMapper;
import com.makeFriends.model.domain.UserInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@DubboService
public class UserInfoImpl implements UserInfoApi {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public void save(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }

    @Override
    public void update(UserInfo userInfo) {
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public UserInfo findById(Long userID) {
        return userInfoMapper.selectById(userID);
    }

    @Override
    public Map<Long, UserInfo> findByIds(List<Long> ids, UserInfo userInfo) {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UserInfo::getId,ids);
        if(userInfo != null){
            wrapper.lt(Objects.isNull(userInfo.getAge()),UserInfo::getAge,userInfo.getAge());
            wrapper.eq(Objects.isNull(userInfo.getGender()),UserInfo::getGender,userInfo.getGender());
        }

        List<UserInfo> list = userInfoMapper.selectList(wrapper);
        Map<Long, UserInfo> map = CollUtil.fieldValueMap(list, "id");
        return map;
    }


}
