package com.codingshuttle.skillora.notification_service.auth;

import com.codingshuttle.skillora.posts_service.auth.UserContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
         Long userId = UserContextHolder.getCurrentUserId();
         if(userId != null){
             requestTemplate.header("X-User-Id", userId.toString());
         }
    }
}
