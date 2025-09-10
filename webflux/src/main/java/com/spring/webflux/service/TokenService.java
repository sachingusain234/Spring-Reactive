package com.spring.webflux.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

import reactor.core.publisher.Mono;
@Service
public class TokenService {

    private final WebClient webClient;

    @Value("${keycloak.token-uri}")
    private String tokenUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.username}")
    private String username;

    @Value("${keycloak.password}")
    private String password;

    @Value("${keycloak.scope}")
    private String scope;

    public TokenService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<String> getAccessToken() {
        System.out.println("Attempting to get token from: " + tokenUri);
        System.out.println("Client ID: " + clientId);
        System.out.println("Username: " + username);

        return webClient.post()
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                                .fromFormData("grant_type", "password")
                                .with("client_id", clientId)
                                .with("client_secret", clientSecret)
                                .with("username", username)
                                .with("password", password)
                         .with("scope", scope)  // comment out for now
                )
                .retrieve()
                .onStatus(status -> status.isError(), response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    System.err.println("Full Keycloak error response: " + body);
                                    return Mono.error(new RuntimeException("Token request error: " + body));
                                })
                )
                .bodyToMono(JsonNode.class)
                .doOnNext(resp -> System.out.println("Token response: " + resp))
                .map(json -> json.get("access_token").asText());
    }

}

