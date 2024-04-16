package com.truongvu.blogrestapi.validate.post;

import com.truongvu.blogrestapi.entity.Post;
import com.truongvu.blogrestapi.validate.handler.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class ValidatePostImage extends ValidationHandler {
    List<String> extensions = new ArrayList<>(List.of("png","jpg","jpeg","pdf"));
    @Override
    public String check(Post post) {
        if(post.getImage() != null) {
            if(!extensions.contains(post.getImage().getType())) {
                return "Image type not correct!";
            }
            if(post.getImage().getData().length == 0) {
                return "Image invalid";
            }
        }
        return checkNext(post);
    }
}
