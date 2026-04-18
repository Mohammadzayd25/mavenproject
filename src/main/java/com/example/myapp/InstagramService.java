package com.example.myapp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InstagramService {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Set<String>> followingByUser = new HashMap<>();
    private final Map<Long, Post> postsById = new HashMap<>();
    private long nextPostId = 1L;

    public User createUser(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be blank");
        }
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }
        User user = new User(username);
        users.put(username, user);
        followingByUser.put(username, new HashSet<>());
        return user;
    }

    public void follow(String followerUsername, String followedUsername) {
        validateKnownUser(followerUsername);
        validateKnownUser(followedUsername);
        if (followerUsername.equals(followedUsername)) {
            throw new IllegalArgumentException("Users cannot follow themselves");
        }
        followingByUser.get(followerUsername).add(followedUsername);
    }

    public long createPost(String authorUsername, String caption) {
        validateKnownUser(authorUsername);
        if (caption == null || caption.isBlank()) {
            throw new IllegalArgumentException("Caption must not be blank");
        }
        long postId = nextPostId++;
        postsById.put(postId, new Post(postId, authorUsername, caption));
        return postId;
    }

    public void likePost(String username, long postId) {
        validateKnownUser(username);
        findPost(postId).like(username);
    }

    public void addComment(String username, long postId, String text) {
        validateKnownUser(username);
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Comment text must not be blank");
        }
        findPost(postId).addComment(new Comment(username, text));
    }

    public List<Post> getFeed(String username) {
        validateKnownUser(username);
        Set<String> followed = followingByUser.get(username);
        return postsById.values().stream()
                .filter(post -> followed.contains(post.authorUsername()))
                .sorted(Comparator.comparingLong(Post::id).reversed())
                .toList();
    }

    public List<Post> getPostsByUser(String username) {
        validateKnownUser(username);
        List<Post> posts = new ArrayList<>();
        for (Post post : postsById.values()) {
            if (post.authorUsername().equals(username)) {
                posts.add(post);
            }
        }
        posts.sort(Comparator.comparingLong(Post::id).reversed());
        return posts;
    }

    private void validateKnownUser(String username) {
        if (!users.containsKey(username)) {
            throw new IllegalArgumentException("Unknown user: " + username);
        }
    }

    private Post findPost(long postId) {
        Post post = postsById.get(postId);
        if (post == null) {
            throw new IllegalArgumentException("Unknown post: " + postId);
        }
        return post;
    }
}
