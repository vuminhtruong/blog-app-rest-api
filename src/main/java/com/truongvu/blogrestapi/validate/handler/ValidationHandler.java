package com.truongvu.blogrestapi.validate.handler;

import com.truongvu.blogrestapi.entity.Post;

public abstract class ValidationHandler {
    private ValidationHandler next;

    public static ValidationHandler link(ValidationHandler first, ValidationHandler... chain) {
        ValidationHandler head = first;
        for (ValidationHandler nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }

        return first;
    }

    public abstract String check(Post post);

    protected String checkNext(Post post) {
        if(next == null) {
            return null;
        }
        return next.check(post);
    }

    @Override
    public String toString() {
        return "ValidationHandler{" +
                "next=" + next +
                '}';
    }
}
