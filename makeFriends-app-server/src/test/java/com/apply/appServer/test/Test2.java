package com.apply.appServer.test;

import com.apply.aotoconfig.template.HuanXinTemplate;
import com.apply.appServer.AppServerApplication;
import com.makeFriends.commons.utils.Constants;
import com.makeFriends.dubbo.api.UserApi;
import com.makeFriends.model.domain.User;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class Test2 {
    @DubboReference
    private UserApi userApi;
    @Autowired
    private HuanXinTemplate template;

    @Test
    public void test(){
        System.out.println(8 >> 3);
        System.out.println(8 << 3);
        System.out.println(10 == 10.0);
    }

    @Test
    public void register() {
        for (int i = 106; i <= 106; i++) {
            User user = userApi.findById(Long.valueOf(i));
            if(user != null) {
                Boolean create = template.createUser("hx" + user.getId(), Constants.INIT_PASSWORD);
                if (create){
                    user.setHxUser("hx" + user.getId());
                    user.setHxPassword(Constants.INIT_PASSWORD);
                    userApi.update(user);
                }
            }
        }
    }
}
