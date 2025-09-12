package com.spring.webflux.controller;

import com.spring.webflux.dto.Role;
import com.spring.webflux.dto.User;
import com.spring.webflux.service.KeycloakUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class KeycloakController {

    private final KeycloakUserRoleService keycloakService;
    @GetMapping("/health-check")
    public String health(){
        return "I am working fine";
    }
    @GetMapping(value = "/users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> getUsers() {
        return keycloakService.getUsers();
    }

    @GetMapping(value = "/roles", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Role> getRoles() {
        return keycloakService.getRoles();
    }
}
