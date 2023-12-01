package com.apply.appServer.controller;

import com.apply.appServer.service.TanhuaService;
import com.makeFriends.model.dto.RecommendUserDto;
import com.makeFriends.model.vo.PageResult;
import com.makeFriends.model.vo.TodayBest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tanhua")
public class TanhuaController {

    @Autowired
    private TanhuaService tanhuaService;

    //今日佳人
    @GetMapping("/todayBest")
    public ResponseEntity todayBest() {
        TodayBest vo = tanhuaService.todayBest();
        return ResponseEntity.ok(vo);
    }
    /**
     * 查询分页推荐好友列表
     */
    @GetMapping("/recommendation")
    public ResponseEntity recommendation(RecommendUserDto dto) {
        PageResult pr = tanhuaService.recommendation(dto);
        return ResponseEntity.ok(pr);
    }

    /**
     * 查看佳人详情
     */
    @GetMapping("/{id}/personalInfo")
    public ResponseEntity personalInfo(@PathVariable("id") Long userId) {
        TodayBest best = tanhuaService.personalInfo(userId);
        return ResponseEntity.ok(best);
    }

    /**
     * 查看陌生人问题
     */
    @GetMapping("/strangerQuestions")
    public ResponseEntity strangerQuestions(Long userId) {
        String questions = tanhuaService.strangerQuestions(userId);
        return ResponseEntity.ok(questions);
    }

    /**
     * 回复陌生人问题
     */
    @PostMapping("/strangerQuestions")
    public ResponseEntity replyQuestions(@RequestBody Map map) {
        //前端传递的userId:是Integer类型的
        String obj = map.get("userId").toString();
        Long userId = Long.valueOf(obj);
        String reply = map.get("reply").toString();
        tanhuaService.replyQuestions(userId,reply);
        return ResponseEntity.ok(null);
    }
}