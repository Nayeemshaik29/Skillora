package com.codingshuttle.skillora.posts_service.service;

import com.codingshuttle.skillora.posts_service.auth.UserContextHolder;
import com.codingshuttle.skillora.posts_service.clients.ConnectionsClient;
import com.codingshuttle.skillora.posts_service.dto.PersonDto;
import com.codingshuttle.skillora.posts_service.dto.PostCreateRequestDto;
import com.codingshuttle.skillora.posts_service.dto.PostDto;
import com.codingshuttle.skillora.posts_service.entity.Post;
import com.codingshuttle.skillora.posts_service.event.PostCreatedEvent;
import com.codingshuttle.skillora.posts_service.exception.ResourceNotFoundException;
import com.codingshuttle.skillora.posts_service.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostsService {

    private final PostsRepository postsRepository;
    private final ModelMapper modelMapper;
    private final ConnectionsClient connectionsClient;
    private final KafkaTemplate<String, PostCreatedEvent> kafkaTemplate;

    public PostDto createPost(PostCreateRequestDto postDto) {
        Long userId = UserContextHolder.getCurrentUserId();
        Post post = modelMapper.map(postDto, Post.class);
        post.setUserId(userId);

        Post savedPost = postsRepository.save(post);
        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                .postId(savedPost.getId())
                .craetorId(userId)
                .content(savedPost.getContent())
                .build();

        kafkaTemplate.send("post-created-topic", postCreatedEvent);
        return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.debug("Retrieving  post with id {}", postId);


        Post post = postsRepository.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("post not found with id: "+postId));
        return modelMapper.map(post, PostDto.class);
    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        log.debug("Retrieving all posts of user with id {}", userId);
        List<Post>  posts = postsRepository.findByUserId(userId);
        return  posts
                .stream()
                .map((element) -> modelMapper.map(element,PostDto.class))
                .collect(Collectors.toList());
    }
}
