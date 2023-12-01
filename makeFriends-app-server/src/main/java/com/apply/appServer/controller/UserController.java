package com.apply.appServer.controller;

import com.apply.appServer.service.UserInfoService;
import com.apply.appServer.utils.UserHolder;
import com.makeFriends.commons.utils.JwtUtils;
import com.makeFriends.model.domain.UserInfo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.Response;
import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("loginReginfo")
    public ResponseEntity loginReginfo(@RequestBody UserInfo userInfo ){
        userInfo.setId(UserHolder.getUserId());
        userInfoService.save(userInfo);
        return ResponseEntity.ok("添加成功");
    }


    @PostMapping("/loginReginfo/head")
    public ResponseEntity head(MultipartFile headPhoto) throws IOException {
        userInfoService.updateHead(headPhoto,UserHolder.getUserId());
        return ResponseEntity.ok("上传成功");
    }
}
