package com.makeFriends.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.makeFriends.dubbo.mapper.UserMapper;
import com.makeFriends.model.domain.User;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@DubboService
public class UserApiImpl implements UserApi{
    @Autowired
    private UserMapper userMapper;
    @Override
    public User findByMobile(String mobile) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getMobile,mobile);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public Long save(User user) {
        userMapper.insert(user);
        return user.getId();
    }

    @Override
    public void update(User user) {
        userMapper.updateById(user);
    }

    @Override
    public User findByHuanxin(String id) {
        return userMapper.selectById(id);
    }

    @Override
    public User findById(Long id) {
        return userMapper.selectById(id);
    }


}
