package com.spring.webflux.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    //private String id;
    private String username;
    private String email;
    // add more fields based on your API response
}
