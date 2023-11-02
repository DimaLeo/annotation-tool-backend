package com.certh.annotationtoolapp.payload.response;

import lombok.Data;

import java.util.List;


@Data
public class AuthenticationResponse {

    private String status;
    private String message;
    private String token;

    public AuthenticationResponse(String token, String status, String message) {
        this.token = token;
        this.status = status;
        this.message = message;
    }
}
