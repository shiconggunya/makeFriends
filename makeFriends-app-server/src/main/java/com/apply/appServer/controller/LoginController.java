package com.apply.appServer.controller;

import com.apply.appServer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Map map){
        String phone = (String) map.get("phone");
        String code = userService.sendMsg(phone);
        return ResponseEntity.ok(code);
    }

    @PostMapping("/loginVerification")
    public ResponseEntity loginVerification(@RequestBody Map map){
        String phone = (String) map.get("phone");
        String code = (String) map.get("verificationCode");
        Map resMap= userService.loginVerification(phone,code);
        return ResponseEntity.ok(resMap);
    }
}
