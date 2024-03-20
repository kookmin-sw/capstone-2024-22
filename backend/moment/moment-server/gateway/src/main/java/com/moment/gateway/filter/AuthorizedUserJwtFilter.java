package com.moment.gateway.filter;

import com.auth0.jwk.JwkException;
import com.moment.gateway.domain.Role;
import com.moment.gateway.utils.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizedUserJwtFilter extends AbstractGatewayFilterFactory<AuthorizedUserJwtFilter.Config> {
    private final JwtTokenUtils jwtTokenUtils;
    public AuthorizedUserJwtFilter(JwtTokenUtils jwtTokenUtils) {
        super(Config.class);
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            log.info("Request Path : " + request.getURI().getPath());
            // Request Header에 Authorization 헤더가 존재하지 않을 때
            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return handleUnAuthorized(exchange); // 401 Error
            }

            // Request Header 에서 token 문자열 받아오기
            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            log.info(authorizationHeader);
            String tokenType = authorizationHeader.split(" ")[0];
            String tokenString = authorizationHeader.split(" ")[1];
            // 토큰 타입 검증
            if(!tokenType.equals("Bearer")) {
                return handleUnAuthorized(exchange); // Bearer인지 확인
            }
            Claims claims = null;
            // 토큰 검증
            try {
                if(!jwtTokenUtils.validate(tokenString)) {
                    return handleUnAuthorized(exchange);
                }
                claims = jwtTokenUtils.getClaims(tokenString);
            } catch (ParseException e) {
                return handleParseError(exchange);
            } catch (JwkException e) {
                return handleJwkError(exchange);
            }

            // token에서 추출
            Long userId = claims.get("userId", Long.class);
            Role role = Role.valueOf(claims.get("role", String.class));
            String sub = claims.getSubject();
            String iss = claims.getIssuer();
            log.info("userId = " + userId);
            log.info("role = " + role);

            // 토큰 ROLE 확인
            if(!role.equals(Role.ROLE_AUTH_USER)){
                // /auth/verify로 들어온 요청일 경우 role이 TemptUser여도 허가
                if(!request.getURI().getPath().equals("/auth/verify")){
                    return HandleInvalidRole(exchange);
                }
            }

            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("userId", userId.toString())
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build()); // 토큰이 일치할 때

        });
    }

    private Mono<Void> handleUnAuthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private Mono<Void> handleParseError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private Mono<Void> handleJwkError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private Mono<Void> HandleInvalidRole(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    @Data
    public static class Config{
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;

    }
}