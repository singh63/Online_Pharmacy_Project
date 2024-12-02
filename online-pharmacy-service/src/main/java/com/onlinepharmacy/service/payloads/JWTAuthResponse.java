package com.onlinepharmacy.service.payloads;

import lombok.Data;

@Data
public class JWTAuthResponse {

    private String token;
    private UserDTO user;
}
