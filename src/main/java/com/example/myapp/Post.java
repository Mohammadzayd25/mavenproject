package com.example.myapp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Post {
    private final long id;
    private final String authorUsername;
    private final String caption;
    private final Set<String> likedBy = new HashSet<>();
    private final List<Comment> comments = new ArrayList<>();

    public Post(long id, String authorUsername, String caption) {
        this.id = id;
        this.authorUsername = authorUsername;
        this.caption = caption;
    }

    public long id() {
        return id;
    }

    public String authorUsername() {
        return authorUsername;
    }

    public String caption() {
        return caption;
    }

    public int likeCount() {
        return likedBy.size();
    }

    public List<Comment> comments() {
        return List.copyOf(comments);
    }

    public void like(String username) {
        likedBy.add(username);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}
