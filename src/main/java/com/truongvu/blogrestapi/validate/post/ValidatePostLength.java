package com.truongvu.blogrestapi.validate.post;

import com.truongvu.blogrestapi.entity.Post;
import com.truongvu.blogrestapi.validate.handler.ValidationHandler;

public class ValidatePostLength extends ValidationHandler {
    @Override
    public String check(Post post) {
        if(post.getDescription().length() < 10 || post.getTitle().length() < 10 || post.getContent().length() < 10) {
            return "Please write more about this post!";
        }
        return checkNext(post);
    }
}
