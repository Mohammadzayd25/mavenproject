package com.example.myapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class AppTest {

    @Test
    public void shouldReturnFeedPostsFromFollowedUsersInReverseChronologicalOrder() {
        InstagramService service = new InstagramService();
        service.createUser("alice");
        service.createUser("bob");
        service.createUser("charlie");
        service.follow("alice", "bob");
        service.follow("alice", "charlie");

        long first = service.createPost("bob", "first");
        long second = service.createPost("charlie", "second");

        assertEquals(2, service.getFeed("alice").size());
        assertEquals(second, service.getFeed("alice").get(0).id());
        assertEquals(first, service.getFeed("alice").get(1).id());
    }

    @Test
    public void shouldTrackLikesAndComments() {
        InstagramService service = new InstagramService();
        service.createUser("alice");
        service.createUser("bob");
        long postId = service.createPost("bob", "trip photo");

        service.likePost("alice", postId);
        service.likePost("alice", postId);
        service.addComment("alice", postId, "Nice!");

        Post post = service.getPostsByUser("bob").get(0);
        assertEquals(1, post.likeCount());
        assertEquals(1, post.comments().size());
        assertEquals("alice", post.comments().get(0).authorUsername());
    }

    @Test
    public void shouldRejectInvalidFollowOperations() {
        InstagramService service = new InstagramService();
        service.createUser("alice");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.follow("alice", "alice"));
        assertEquals("Users cannot follow themselves", ex.getMessage());
    }
}
