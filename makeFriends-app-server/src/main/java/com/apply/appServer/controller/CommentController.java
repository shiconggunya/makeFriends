package com.apply.appServer.controller;

import com.apply.appServer.service.CommentService;
import com.makeFriends.model.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentsService;


    //分页查询评理列表
    @GetMapping
    public ResponseEntity findComments(@RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer pagesize,
                                       String movementId) {
        PageResult pr = commentsService.findComments(movementId,page,pagesize);
        return ResponseEntity.ok(pr);
    }

    /**
     * 发布评论
     */
    @PostMapping
    public ResponseEntity saveComments(@RequestBody Map map) {
        String movementId = (String )map.get("movementId");
        String comment = (String)map.get("comment");
        commentsService.saveComments(movementId,comment);
        return ResponseEntity.ok(null);
    }



}
