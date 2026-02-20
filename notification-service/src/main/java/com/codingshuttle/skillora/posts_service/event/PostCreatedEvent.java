package com.codingshuttle.skillora.posts_service.event;


import lombok.Builder;
import lombok.Data;

@Data
public class PostCreatedEvent {
    Long craetorId;
    String content;
    Long postId;
}
