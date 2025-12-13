package com.codingshuttle.skillora.posts_service.repository;

import com.codingshuttle.skillora.posts_service.entity.PostLike;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {

    boolean existsByUserIdAndPostId(Long userId,Long postId);

    @Transactional
    void deleteByUserIdAndPostId(long userId, Long postId);
}
