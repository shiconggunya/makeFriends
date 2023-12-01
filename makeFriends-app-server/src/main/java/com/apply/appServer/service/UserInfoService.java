package com.apply.appServer.service;

import com.apply.aotoconfig.template.AipFaceTemplate;
import com.apply.aotoconfig.template.OssTemplate;
import com.apply.appServer.exception.BusinessException;
import com.makeFriends.dubbo.api.UserInfoApi;
import com.makeFriends.model.domain.UserInfo;
import com.makeFriends.model.vo.ErrorResult;
import com.makeFriends.model.vo.UserInfoVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.BindException;
import java.rmi.RemoteException;

@Service
public class UserInfoService {
    @DubboReference
    private UserInfoApi userInfoApi;
    @Autowired
    private OssTemplate ossTemplate;
    @Autowired
    private AipFaceTemplate aipFaceTemplate;

    public void save(UserInfo userInfo) {
        userInfoApi.save(userInfo);
    }
    /*
        更新头像
     */
    public void updateHead(MultipartFile headPhoto, Long id) throws IOException {
        String url = ossTemplate.upload(headPhoto.getOriginalFilename(), headPhoto.getInputStream());
        if (!aipFaceTemplate.detect(url)) throw new BusinessException(ErrorResult.faceError());
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setAvatar(url);
        userInfoApi.update(userInfo);
    }

    public UserInfoVo findById(Long userID) {
        UserInfo userInfo = userInfoApi.findById(userID);
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo,userInfoVo);
        if(userInfo.getAge()!=null) userInfoVo.setAge(String.valueOf(userInfo.getAge()));
        return userInfoVo;
    }

    public void update(UserInfo userInfo) {
        userInfoApi.update(userInfo);
    }
}
