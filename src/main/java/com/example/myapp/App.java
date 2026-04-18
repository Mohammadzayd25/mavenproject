package com.example.myapp;

public class App {
    public static void main(String[] args) {
        InstagramService service = new InstagramService();

        service.createUser("alice");
        service.createUser("bob");
        service.follow("alice", "bob");

        long postId = service.createPost("bob", "Sunset at the beach #travel");
        service.likePost("alice", postId);
        service.addComment("alice", postId, "Amazing shot!");

        System.out.println("=== Alice feed ===");
        for (Post post : service.getFeed("alice")) {
            System.out.printf(
                    "@%s: %s (likes=%d, comments=%d)%n",
                    post.authorUsername(),
                    post.caption(),
                    post.likeCount(),
                    post.comments().size());
        }
    }
}
