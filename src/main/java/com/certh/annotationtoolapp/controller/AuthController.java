package com.certh.annotationtoolapp.controller;

import com.certh.annotationtoolapp.model.user.User;
import com.certh.annotationtoolapp.payload.request.AuthRequest;
import com.certh.annotationtoolapp.payload.request.ValidateTokenRequest;
import com.certh.annotationtoolapp.payload.response.AuthenticationResponse;
import com.certh.annotationtoolapp.payload.response.GeneralResponse;
import com.certh.annotationtoolapp.security.jwt.JwtUtils;
import com.certh.annotationtoolapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestBody.getUsername(), requestBody.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userService.findUserByUsername(requestBody.getUsername());

            System.out.println(user.getUsername() + " trying to log in");


            String jwt = jwtUtils.generateToken(authentication, "jwt");

            return ResponseEntity.ok(
                    new AuthenticationResponse(
                            jwt,
                            "Success",
                            "Success")
            );
        }
        catch (Exception e){
//            AuthenticationResponse errorResponse = new AuthenticationResponse("", "", "Unauthorized", "Invalid Credentials");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/validateToken")
    public ResponseEntity<AuthenticationResponse> validateToken(@RequestBody ValidateTokenRequest requestBody){

        AuthenticationResponse response;


        if(!jwtUtils.isJwtTokenExpired(requestBody.getAccessToken())){
            response = new AuthenticationResponse("", "Success", "The token is still valid");
        }else {
            response = new AuthenticationResponse("","Expired", "The token is expired");

        }

        System.out.println(response.getStatus());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}


