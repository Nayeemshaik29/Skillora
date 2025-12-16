package com.codingshuttle.skillora.posts_service.controller;


import com.codingshuttle.skillora.posts_service.auth.UserContextHolder;
import com.codingshuttle.skillora.posts_service.dto.PostCreateRequestDto;
import com.codingshuttle.skillora.posts_service.dto.PostDto;
import com.codingshuttle.skillora.posts_service.entity.Post;
import com.codingshuttle.skillora.posts_service.service.PostsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;


    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postDto, HttpServletRequest httpServletRequest){
        PostDto createPostDto = postsService.createPost(postDto , 1L);
        return new  ResponseEntity<>(createPostDto, HttpStatus.CREATED);

    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {



        PostDto postDto = postsService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/users/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getAllPosts(@PathVariable Long userId) {
        List<PostDto> posts = postsService.getAllPostsOfUser(userId);
        return ResponseEntity.ok(posts);
    }
}
