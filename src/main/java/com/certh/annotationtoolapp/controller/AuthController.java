package com.certh.annotationtoolapp.controller;

import com.certh.annotationtoolapp.payload.request.AuthRequest;
import com.certh.annotationtoolapp.payload.response.GeneralResponse;
import com.certh.annotationtoolapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/user")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse> createUser(@RequestBody AuthRequest requestBody){

        GeneralResponse response = userService.createUser(requestBody.getUsername(), requestBody.getPassword());
        return new ResponseEntity<>(response, response.getStatus().equals("success")? HttpStatus.CREATED: HttpStatus.OK);

    }

    @PostMapping("/authenticate")
    public ResponseEntity<GeneralResponse> authenticateUser(@RequestBody AuthRequest requestBody){

        GeneralResponse response = userService.authenticateUser(requestBody.getUsername(), requestBody.getPassword());
        return new ResponseEntity<>(response, response.getStatus().equals("success")? HttpStatus.ACCEPTED: HttpStatus.OK);

    }
}


