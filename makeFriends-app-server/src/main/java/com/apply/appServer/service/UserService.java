package com.apply.appServer.service;

import com.apply.aotoconfig.template.HuanXinTemplate;
import com.apply.aotoconfig.template.SmsTemplate;
import com.apply.appServer.exception.BusinessException;
import com.makeFriends.commons.utils.Constants;
import com.makeFriends.commons.utils.JwtUtils;
import com.makeFriends.dubbo.api.UserApi;
import com.makeFriends.model.domain.User;
import com.makeFriends.model.vo.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
@Service
@Slf4j
public class UserService {

    @Autowired
    private SmsTemplate template;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private HuanXinTemplate huanXinTemplate;

    @DubboReference
    private UserApi userApi;

    /**
     * 发送短信验证码
     * @param phone
     */
    public String sendMsg(String phone) {
        //1、随机生成6位数字
        String code = RandomStringUtils.randomNumeric(6);
        //2、调用template对象，发送手机短信
        //template.sendSms(phone,code);
        log.info("code:{}",code);
        //3、将验证码存入到redis
        redisTemplate.opsForValue().set("CHECK_CODE_"+phone,code, Duration.ofMinutes(5));
        return code;
    }

    /**
     * 验证登录
     * @param phone
     * @param code
     */
    public Map loginVerification(String phone, String code) {
        //1、从redis中获取下发的验证码
        String redisCode = redisTemplate.opsForValue().get("CHECK_CODE_" + phone);
        //2、对验证码进行校验（验证码是否存在，是否和输入的验证码一致）
        if(StringUtils.isEmpty(redisCode) || !redisCode.equals(code)) {
            //验证码无效
            throw new BusinessException(ErrorResult.loginError());
        }
        //3、删除redis中的验证码
        redisTemplate.delete("CHECK_CODE_" + phone);
        //4、通过手机号码查询用户
        User user = userApi.findByMobile(phone);
        boolean isNew = false;
        //5、如果用户不存在，创建用户保存到数据库中
        if(user == null) {
            user = new User();
            user.setMobile(phone);
            user.setPassword(DigestUtils.md5Hex("123456"));
            Long userId = userApi.save(user);
            user.setId(userId);
            isNew = true;

            //注册环信用户
            String hxUser = "hx"+user.getId();
            Boolean create = huanXinTemplate.createUser(hxUser, Constants.INIT_PASSWORD);
            if(create) {
                user.setHxUser(hxUser);
                user.setHxPassword(Constants.INIT_PASSWORD);
                userApi.update(user);
            }
        }
        //6、通过JWT生成token(存入id和手机号码)
        Map tokenMap = new HashMap();
        tokenMap.put("id",user.getId());
        tokenMap.put("mobile",phone);
        String token = JwtUtils.getToken(tokenMap);
        //7、构造返回值
        Map retMap = new HashMap();
        retMap.put("token",token);
        retMap.put("isNew",isNew);

        return retMap;
    }
}
