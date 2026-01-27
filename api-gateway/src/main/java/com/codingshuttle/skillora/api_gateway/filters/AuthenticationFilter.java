package com.codingshuttle.skillora.api_gateway.filters;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;


@Slf4j
@Component
public class AuthenticationFilter  extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtService jwtService;


    public AuthenticationFilter(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            log.info("Login request: {}",exchange.getRequest().getURI());
            final String tokenHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

            if(tokenHeader == null || !tokenHeader.startsWith("Bearer ")){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                log.info("Authorization header not found");
                return exchange.getResponse().setComplete();

            }
            final String token = tokenHeader.split("Bearer ")[1];
            try {
                String userId = jwtService.getUserIdFromToken(token);
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header("X-User-Id", userId.toString())
                        .build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            }catch(JwtException e)
            {
                log.info("JWT Exception: {}",e.getLocalizedMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };

    }

    public static class  Config {

    }
}
