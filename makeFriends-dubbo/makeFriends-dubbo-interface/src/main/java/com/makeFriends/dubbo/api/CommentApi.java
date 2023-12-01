package com.makeFriends.dubbo.api;

import com.makeFriends.model.enums.CommentType;
import com.makeFriends.model.mongdb.Comment;

import java.util.List;

public interface CommentApi {
    List<Comment> findComments(String movementId, CommentType commentType, Integer page, Integer pagesize);

    Integer save(Comment comment1);

    Boolean hasComment(String movementId, Long userId, CommentType commentType);

    Integer delete(Comment comment);
}
