package com.codingshuttle.skillora.posts_service.service;


import com.codingshuttle.skillora.posts_service.auth.UserContextHolder;
import com.codingshuttle.skillora.posts_service.entity.Post;
import com.codingshuttle.skillora.posts_service.entity.PostLike;
import com.codingshuttle.skillora.posts_service.event.PostLikedEvent;
import com.codingshuttle.skillora.posts_service.exception.BadRequestException;
import com.codingshuttle.skillora.posts_service.exception.ResourceNotFoundException;
import com.codingshuttle.skillora.posts_service.repository.PostLikeRepository;
import com.codingshuttle.skillora.posts_service.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostsRepository postsRepository;
    private final KafkaTemplate<Long,PostLikedEvent>  kafkaTemplate;

    public void likePost(@PathVariable Long postId){
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Attempting to like post like id:{}",postId);

        Post post = postsRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Post not found with id: " + postId
                ));

        boolean alreadyliked = postLikeRepository.existsByUserIdAndPostId(userId,postId);
        if(alreadyliked) throw new BadRequestException("Cannot like the same post again.");

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);
        log.info("Post with id :{} liked successfully",postId);
        PostLikedEvent postLikedEvent = PostLikedEvent.builder()
                .postId(userId)
                .likedByUserId(userId)
                .creatorId(post.getUserId()).build();

        kafkaTemplate.send("post-liked-event",postId,postLikedEvent);

    }

    public void unlikePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Attempting to unlike post like id:{}",postId);
        boolean exists = postsRepository.existsById(postId);
        if(!exists) throw new ResourceNotFoundException("Post not found with id:"+postId);
        boolean alreadyliked = postLikeRepository.existsByUserIdAndPostId(userId,postId);
        if(!alreadyliked) throw new BadRequestException("Cannot unlike the  post which is not liked.");
        postLikeRepository.deleteByUserIdAndPostId(userId,postId);

        log.info("Post with id :{} unliked successfully",postId);

    }
}
