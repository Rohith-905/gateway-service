package com.gatewayService;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/auth/addUser",
            "/auth/generateToken",
            "/securityService/swagger-ui.html",
            "/securityService/swagger-ui/**",
            "/crossCutting/swagger-ui.html",
            "/crossCutting/swagger-ui/**",
            "/issuerService/swagger-ui.html",
            "/issuerService/swagger-ui/**",
            "/bookService/swagger-ui.html",
            "/bookService/swagger-ui/**",
            "/auth/swagger-ui/**",
            "/auth/swagger-ui.html",
            "/actuator/**",
            "/v3/api-docs/**"
    );

    public Predicate<ServerHttpRequest> isSecured = request -> openApiEndpoints.stream()
            .noneMatch(uri -> request.getURI().getPath().contains(uri));
}