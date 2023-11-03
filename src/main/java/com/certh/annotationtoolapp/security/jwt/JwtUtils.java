package com.certh.annotationtoolapp.security.jwt;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.*;

import com.certh.annotationtoolapp.security.services.UserDetailsImpl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
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

    @Value("${jwt.refresh.secret.value}")
    private String jwtRefreshSecret;

    @Value("${token.refresh.expiration.minutes}")
    private int jwtRefreshExpirationMinutes;

    public String generateToken(Authentication authentication, String type) {
        switch (type) {
            case "jwt" -> {
                return generateJwtToken(authentication);
            }
            case "refresh" -> {
                return generateJwtRefreshToken(authentication);
            }
        }
        return null;
    }

    private String generateJwtRefreshToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        byte[] signingKey = jwtRefreshSecret.getBytes();

        return Jwts.builder()
                .setHeaderParam("typ", TOKEN_TYPE)
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS256)
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(jwtRefreshExpirationMinutes).toInstant()))
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setId(UUID.randomUUID().toString())
                .setIssuer(TOKEN_ISSUER)
                .setAudience(TOKEN_AUDIENCE)
                .setSubject(userPrincipal.getUsername())
                .compact();

    }

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = new ArrayList<>();
        roles.add("User");

        byte[] signingKey = jwtSecret.getBytes();

        return Jwts.builder()
                .setHeaderParam("typ", TOKEN_TYPE)
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS256)
                .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(jwtExpirationMinutes).toInstant()))
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setId(UUID.randomUUID().toString())
                .setIssuer(TOKEN_ISSUER)
                .setAudience(TOKEN_AUDIENCE)
                .setSubject(userPrincipal.getUsername())
                .claim("roles", roles)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
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
}
