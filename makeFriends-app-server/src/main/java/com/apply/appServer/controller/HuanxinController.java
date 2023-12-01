package com.apply.appServer.controller;

import com.apply.appServer.service.HuanxinService;
import com.makeFriends.model.vo.HuanXinUserVo;
import com.makeFriends.model.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/huanxin")
public class HuanxinController {

    @Autowired
    private HuanxinService  huanxinService;

    @GetMapping("/user")
    public ResponseEntity userinfo(Long huanxinId) {
        HuanXinUserVo vo = huanxinService.findUserInfoByHuanxin();
        return ResponseEntity.ok(vo);
    }
}