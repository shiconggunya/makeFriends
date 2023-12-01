package com.makeFriends.dubbo.api;

import com.makeFriends.model.domain.Question;

public interface QuestionApi {
    Question findByUserId(Long userId);

    void save(Question question);

    void update(Question question);
}
