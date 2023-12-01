package com.apply.appServer.test;

import com.apply.aotoconfig.template.OssTemplate;
import com.apply.appServer.AppServerApplication;
import com.makeFriends.dubbo.api.UserApi;
import com.makeFriends.model.domain.User;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class Test1 {
    @DubboReference
    private UserApi userApi;
    @Autowired
    private OssTemplate ossTemplate;
    @Test
    public void test1(){
        User byMobile = userApi.findByMobile("13800138000");
        System.out.println(byMobile);
    }

    @Test
    public void test2() throws FileNotFoundException {
        String path = "C:\\Users\\a\\Pictures\\002.jpg";
        FileInputStream fileInputStream = new FileInputStream(path);
        ossTemplate.upload(path,fileInputStream);
    }
}
