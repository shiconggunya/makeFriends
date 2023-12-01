package com.apply.appServer.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.apply.aotoconfig.template.OssTemplate;
import com.apply.appServer.exception.BusinessException;
import com.apply.appServer.utils.UserHolder;
import com.makeFriends.commons.utils.Constants;
import com.makeFriends.dubbo.api.MovementApi;
import com.makeFriends.dubbo.api.UserInfoApi;
import com.makeFriends.model.domain.UserInfo;
import com.makeFriends.model.mongdb.Movement;
import com.makeFriends.model.vo.ErrorResult;
import com.makeFriends.model.vo.MovementsVo;
import com.makeFriends.model.vo.PageResult;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovementsService {

    @Autowired
    private OssTemplate ossTemplate;

    @DubboReference
    private MovementApi movementApi;

    @DubboReference
    private UserInfoApi userInfoApi;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 发布动态
     */
    public void publishMovement(Movement movement, MultipartFile[] imageContent) throws IOException {
        //1、判断发布动态的内容是否存在
        if (StringUtils.isEmpty(movement.getTextContent())) {
            throw new BusinessException(ErrorResult.contentError());
        }
        //2、获取当前登录的用户id
        Long userId = UserHolder.getUserId();
        //3、将文件内容上传到阿里云OSS，获取请求地址
        List<String> medias = new ArrayList<>();
        for (MultipartFile multipartFile : imageContent) {
            String upload = ossTemplate.upload(multipartFile.getOriginalFilename(), multipartFile.getInputStream());
            medias.add(upload);
        }
        //4、将数据封装到Movement对象
        movement.setUserId(userId);
        movement.setMedias(medias);
        //5、调用API完成发布动态
        movementApi.publish(movement);
    }


    //查询个人动态
    public PageResult findByUserId(Long userId, Integer page, Integer pagesize) {
        //1、根据用户id，调用API查询个人动态内容（PageResult  -- Movement）
        PageResult pr = movementApi.findByUserId(userId, page, pagesize);
        //2、获取PageResult中的item列表对象
        List<Movement> items = (List<Movement>) pr.getItems();
        //3、非空判断
        if (items == null) {
            return pr;
        }
        //4、循环数据列表
        UserInfo userInfo = userInfoApi.findById(userId);
        List<MovementsVo> vos = new ArrayList<>();
        for (Movement item : items) {
            //5、一个Movement构建一个Vo对象
            MovementsVo vo = MovementsVo.init(userInfo, item);
            vos.add(vo);
        }
        //6、构建返回值
        pr.setItems(vos);
        return pr;
    }

    //好友动态
    public PageResult findFriendMovements(Integer page, Integer pagesize) {
        //1、获取当前用户id
        Long userId = UserHolder.getUserId();
        //2、查询数据列表
        List<Movement> items = movementApi.findFriendMovements(userId, page, pagesize);
        return getPageResult(page, pagesize, items);
    }

    //推荐动态
    public PageResult findRecommendMovements(Integer page, Integer pagesize) {
        String redisKey = Constants.MOVEMENTS_INTERACT_KEY + UserHolder.getUserId();
        String redisData = this.redisTemplate.opsForValue().get(redisKey);
        List<Movement> list = null;
        if (StringUtils.isEmpty(redisData)) {
            list = movementApi.randomMovements(pagesize);
        } else {
            String[] split = redisData.split(",");
            if ((page - 1) * pagesize > split.length) {
                return new PageResult();
            }
            List<Long> pids = Arrays.stream(split)
                    .skip((page - 1) * pagesize)
                    .limit(pagesize)
                    .map(e -> Convert.toLong(e))
                    .collect(Collectors.toList());
            list = movementApi.findByPids(pids);

        }
        return getPageResult(page, pagesize, list);
    }

    private PageResult getPageResult(Integer page, Integer pagesize, List<Movement> items) {
        //3、非空判断
        if (CollUtil.isEmpty(items)) {
            return new PageResult();
        }
        //4、获取好友用户id
        List<Long> userIds = CollUtil.getFieldValues(items, "userId", Long.class);
        //5、循环数据列表
        Map<Long, UserInfo> userMaps = userInfoApi.findByIds(userIds, null);
        List<MovementsVo> vos = new ArrayList<>();
        for (Movement item : items) {
            //5、一个Movement构建一个Vo对象
            UserInfo userInfo = userMaps.get(item.getUserId());
            MovementsVo vo = MovementsVo.init(userInfo, item);
            vos.add(vo);
        }
        //6、构建返回值
        return new PageResult(page, pagesize, 0, vos);
    }

    public MovementsVo findMovementById(String movementId) {
        Movement movements = movementApi.findById(movementId);
        if (movements == null) {
            return null;
        } else {
            UserInfo userInfo = userInfoApi.findById(movements.getUserId());
            return MovementsVo.init(userInfo, movements);
        }
    }
}