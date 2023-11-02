package com.certh.annotationtoolapp.controller;

import com.certh.annotationtoolapp.model.usecase.Usecase;
import com.certh.annotationtoolapp.service.UsecaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/usecases")
public class UsecaseController {
    private final UsecaseService usecaseService;

    @Autowired
    public UsecaseController(UsecaseService usecaseService) {
        this.usecaseService = usecaseService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Usecase>> getAllUsecases() {
        List<Usecase> usecases = usecaseService.getAllUsecases();
        return new ResponseEntity<>(usecases, HttpStatus.OK);
    }
}
