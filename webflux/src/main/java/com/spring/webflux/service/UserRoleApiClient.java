package com.spring.webflux.service;

import com.spring.webflux.dto.Role;
import com.spring.webflux.dto.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class UserRoleApiClient {

    private final WebClient webClient;

    public UserRoleApiClient(WebClient.Builder builder, @Value("${api.base-url}") String apiBaseUrl) {
        this.webClient = builder.baseUrl(apiBaseUrl).build();
    }

    public Flux<User> getUsers(String token) {
        return webClient.get()
                .uri("/admin/master/console/#/apwrd/users")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToFlux(User.class);
    }

    public Flux<Role> getRoles(String token) {
        return webClient.get()
                .uri("admin/master/console/#/apwrd/roles")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToFlux(Role.class);
    }
}