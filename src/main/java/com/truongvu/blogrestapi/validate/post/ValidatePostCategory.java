package com.truongvu.blogrestapi.validate.post;

import com.truongvu.blogrestapi.entity.Post;
import com.truongvu.blogrestapi.validate.handler.ValidationHandler;

public class ValidatePostCategory extends ValidationHandler {
    @Override
    public String check(Post post) {
        if(post.getCategory() == null) {
            return "Please choose the category for this post";
        }
        return checkNext(post);
    }
}
