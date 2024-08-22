package com.gatewayService;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import jakarta.ws.rs.core.HttpHeaders;

@Component
@Order(1)
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {
    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);

    public JwtFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
        	ServerHttpRequest request = exchange.getRequest();
            if (routeValidator.isSecured.test(request)) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                     throw new RuntimeException("Missing authorization header");
                }

                String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                    try {
                        jwtUtil.validateToken(token);
                        
                    } catch (Exception e) {
                        LOGGER.error("Invalid access!", e);
                       throw new RuntimeException("Token is invalid : "+e.getMessage());
                    }
                } else {
                    throw new RuntimeException("Invalid authorization header format");
                }
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}