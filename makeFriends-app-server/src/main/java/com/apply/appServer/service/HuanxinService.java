package com.apply.appServer.service;

import com.apply.aotoconfig.template.HuanXinTemplate;
import com.apply.appServer.utils.UserHolder;
import com.makeFriends.dubbo.api.UserApi;
import com.makeFriends.dubbo.api.UserInfoApi;
import com.makeFriends.model.domain.User;
import com.makeFriends.model.domain.UserInfo;
import com.makeFriends.model.vo.HuanXinUserVo;
import com.makeFriends.model.vo.UserInfoVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HuanxinService {

    @DubboReference
    private UserApi userApi;

    @DubboReference
    private UserInfoApi userInfoApi;


    @Autowired
    private HuanXinTemplate huanXinTemplate;

    /**
     * 根据环信id查询用户详情
     */
    public HuanXinUserVo findUserInfoByHuanxin() {
        Long userId = UserHolder.getUserId();
        //1、根据环信id查询用户
        User user = userApi.findByHuanxin(String.valueOf(userId));
        if(user == null) return  null;
        return new HuanXinUserVo(user.getHxUser(),user.getHxPassword());

    }
}
