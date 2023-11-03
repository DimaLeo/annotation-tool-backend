package com.certh.annotationtoolapp.controller;

import com.certh.annotationtoolapp.model.user.User;
import com.certh.annotationtoolapp.payload.request.AuthRequest;
import com.certh.annotationtoolapp.payload.response.AuthenticationResponse;
import com.certh.annotationtoolapp.payload.response.GeneralResponse;
import com.certh.annotationtoolapp.security.jwt.JwtUtils;
import com.certh.annotationtoolapp.security.services.UserDetailsImpl;
import com.certh.annotationtoolapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
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
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthRequest requestBody){

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestBody.getUsername(), requestBody.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userService.findUserByUsername(requestBody.getUsername());

//            if(user.getRefreshToken() != null){
//
//                jwtUtils.validateJwtToken(user.getRefreshToken());
//
//
//
//            }


            String jwt = jwtUtils.generateToken(authentication, "jwt");

            return ResponseEntity.ok(
                    new AuthenticationResponse(
                            jwt,
                            "Success",
                            "Success")
            );
        }
        catch (Exception e){
            AuthenticationResponse response = new AuthenticationResponse("", "Unauthorized", "Invalid Credentials");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }



    }
}


