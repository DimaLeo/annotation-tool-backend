package com.certh.annotationtoolapp.payload.request;

import lombok.Getter;

@Getter
public class AuthRequest {
    private String username;
    private String password;
}
