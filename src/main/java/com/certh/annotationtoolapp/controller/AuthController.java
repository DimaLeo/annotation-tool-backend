package com.certh.annotationtoolapp.controller;

import com.certh.annotationtoolapp.payload.request.AuthRequest;
import com.certh.annotationtoolapp.payload.request.ValidateTokenRequest;
import com.certh.annotationtoolapp.payload.response.AuthenticationResponse;
import com.certh.annotationtoolapp.payload.response.GeneralResponse;
import com.certh.annotationtoolapp.security.jwt.JwtUtils;
import com.certh.annotationtoolapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse> createUser(@RequestBody AuthRequest requestBody){

        GeneralResponse response = userService.createUser(requestBody.getUsername(), requestBody.getPassword());
        return new ResponseEntity<>(response, response.getStatus().equals("success")? HttpStatus.CREATED: HttpStatus.OK);

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthRequest requestBody) {

        log.info("User authentication initiated");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestBody.getUsername(), requestBody.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("Authenticated");

            log.info("Generating accessToken");

            String jwt = jwtUtils.generateToken(authentication, "jwt");

            log.info("Successfully generated accessToken.");

            return ResponseEntity.ok(
                    new AuthenticationResponse(
                            jwt,
                            "Success",
                            "Success")
            );
        }
        catch (Exception e){
//            AuthenticationResponse errorResponse = new AuthenticationResponse("", "", "Unauthorized", "Invalid Credentials");
            log.info("Unauthorized");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/validateToken")
    public ResponseEntity<AuthenticationResponse> validateToken(@RequestBody ValidateTokenRequest requestBody){

        log.info("Token validation initiated");
        AuthenticationResponse response;


        log.info("Check if token is expired.");
        if(!jwtUtils.isJwtTokenExpired(requestBody.getAccessToken())){
            log.info("Token still active");
            response = new AuthenticationResponse("", "Success", "The token is still valid");
        }else {
            log.info("Token was expired");
            response = new AuthenticationResponse("","Expired", "The token is expired");

        }

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}


