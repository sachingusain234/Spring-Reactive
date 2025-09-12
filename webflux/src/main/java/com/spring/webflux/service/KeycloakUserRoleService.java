package com.spring.webflux.service;

import com.spring.webflux.dto.Role;
import com.spring.webflux.dto.User;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
//@RequiredArgsConstructor
public class KeycloakUserRoleService {

    // Keycloak config — better to move these to application.properties later
    private final String serverUrl = "http://localhost:8080";
    private final String realm = "master";
    private final String clientId = "admin-cli";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private Keycloak getKeycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .username(ADMIN_USERNAME)
                .password(ADMIN_PASSWORD)
                .build();
    }

    /**
     * Fetch all users from Keycloak and map to internal User DTO
     */
    public Flux<User> getUsers() {
        return Mono.fromCallable(() -> {
            try (Keycloak keycloak = getKeycloakAdminClient()) {
                List<UserRepresentation> users = keycloak.realm(realm).users().list();

                return users.stream()
                        .map(userRep -> User.builder()
                                .id(userRep.getId())
                                .username(userRep.getUsername())
                                .email(userRep.getEmail())
                                .firstName(userRep.getFirstName())
                                .lastName(userRep.getLastName())
                                .enabled(userRep.isEnabled())
                                .build())
                        .toList();
            }
        }).flatMapMany(Flux::fromIterable);
    }

    /**
     * Fetch all roles from Keycloak and map to internal Role DTO
     */
    public Flux<Role> getRoles() {
        return Mono.fromCallable(() -> {
            try (Keycloak keycloak = getKeycloakAdminClient()) {
                List<RoleRepresentation> roles = keycloak.realm(realm).roles().list();

                return roles.stream()
                        .map(roleRep -> Role.builder()
                                .id(roleRep.getId())
                                .name(roleRep.getName())
                                .description(roleRep.getDescription())
                                .build())
                        .toList();
            }
        }).flatMapMany(Flux::fromIterable);
    }
//    public Flux<User> getUsers() {
//        return Mono.fromCallable(() -> {
//                    try (Keycloak keycloak = KeycloakBuilder.builder()
//                            .serverUrl("http://localhost:8080")
//                            .realm("apwrd")
//                            .clientId("admin-cli")
//                            .username("DGoswami2")
//                            .password("APWRDeportalv2")
//                            .build()) {
//
//                        List<UserRepresentation> users = keycloak.realm("apwrd").users().list();
//
//                        return users.stream()
//                                .map(userRep -> User.builder()
//                                        .id(userRep.getId())
//                                        .username(userRep.getUsername())
//                                        .email(userRep.getEmail())
//                                        .firstName(userRep.getFirstName())
//                                        .lastName(userRep.getLastName())
//                                        .enabled(userRep.isEnabled())
//                                        .build())
//                                .toList();
//                    }
//                }).subscribeOn(Schedulers.boundedElastic())
//                .flatMapMany(Flux::fromIterable);
//    }

}
