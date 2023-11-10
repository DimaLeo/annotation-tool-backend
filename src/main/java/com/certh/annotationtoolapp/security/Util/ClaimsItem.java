package com.certh.annotationtoolapp.security.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.Data;

import java.util.Optional;


@Data
public class ClaimsItem {
    private Optional<Jws<Claims>> jws;
    private Boolean valid;
    private String message;
}
