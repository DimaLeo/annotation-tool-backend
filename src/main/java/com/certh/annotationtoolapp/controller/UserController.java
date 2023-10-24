package com.certh.annotationtoolapp.controller;

import com.certh.annotationtoolapp.requests.UserRequest;
import com.certh.annotationtoolapp.responses.GeneralResponse;
import com.certh.annotationtoolapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse> createUser(@RequestBody UserRequest requestBody){

        GeneralResponse response = userService.createUser(requestBody.getUsername(), requestBody.getPassword());
        return new ResponseEntity<>(response, response.getStatus().equals("success")? HttpStatus.CREATED: HttpStatus.BAD_GATEWAY);

    }

    @PostMapping("/authenticate")
    public ResponseEntity<GeneralResponse> authenticateUser(@RequestBody UserRequest requestBody){

        GeneralResponse response = userService.authenticateUser(requestBody.getUsername(), requestBody.getPassword());
        return new ResponseEntity<>(response, response.getStatus().equals("success")? HttpStatus.CREATED: HttpStatus.UNAUTHORIZED);

    }
}


