package com.spring.webflux.controller;

import com.spring.webflux.dto.Role;
import com.spring.webflux.dto.User;
import com.spring.webflux.service.TokenService;
import com.spring.webflux.service.UserRoleApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class UserRoleController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRoleApiClient userRoleApiClient;

    @GetMapping("/users")
    public Flux<User> getUsers() {
        return tokenService.getAccessToken()
                .flatMapMany(token -> userRoleApiClient.getUsers(token))
                .doOnError(error -> System.err.println("Error fetching users: " + error.getMessage()))
                .onErrorResume(error -> {
                    System.err.println("Failed to get users: " + error.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/roles")
    public Flux<Role> getRoles() {
        return tokenService.getAccessToken()
                .flatMapMany(token -> userRoleApiClient.getRoles(token))
                .doOnError(error -> System.err.println("Error fetching roles: " + error.getMessage()))
                .onErrorResume(error -> {
                    System.err.println("Failed to get roles: " + error.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/test-token")
    public Mono<String> testToken() {
        return tokenService.getAccessToken()
                .doOnNext(token -> System.out.println("Token received: " + token.substring(0, Math.min(20, token.length())) + "..."))
                .doOnError(error -> System.err.println("Token test failed: " + error.getMessage()));
    }

}

