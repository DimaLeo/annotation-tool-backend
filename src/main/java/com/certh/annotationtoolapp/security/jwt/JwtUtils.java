package com.certh.annotationtoolapp.security.jwt;

import java.time.ZonedDateTime;
import java.util.*;

import com.certh.annotationtoolapp.security.services.UserDetailsImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Slf4j
@Component
public class JwtUtils {
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "annotation-tool-api";
    public static final String TOKEN_AUDIENCE = "annotation-tool-ui";

    @Value("${jwt.secret.value}")
    private String jwtSecret;

    @Value("${token.expiration.minutes}")
    private int jwtExpirationMinutes;

    public String generateToken(Authentication authentication, String type) {
        if (type.equals("jwt")) {
            return generateJwtToken(authentication);
        }
        return null;
    }


    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = new ArrayList<>();
        roles.add("User");

        byte[] signingKey = jwtSecret.getBytes();

        return Jwts.builder()
                .setHeaderParam("typ", TOKEN_TYPE)
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS256)
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(jwtExpirationMinutes).toInstant()))
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setId(UUID.randomUUID().toString())
                .setIssuer(TOKEN_ISSUER)
                .setAudience(TOKEN_AUDIENCE)
                .setSubject(userPrincipal.getUsername())
                .claim("rol", roles)
                .compact();
    }

    public Optional<Jws<Claims>> getJwtClaims(String authToken) {
        try {

            byte[] signingKey = jwtSecret.getBytes();


            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(authToken);

            return Optional.of(jws);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return Optional.empty();
    }

    public boolean isJwtTokenExpired(String accessToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }


}
