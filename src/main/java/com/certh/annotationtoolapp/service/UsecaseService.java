package com.certh.annotationtoolapp.service;

import com.certh.annotationtoolapp.model.usecase.Usecase;
import com.certh.annotationtoolapp.repository.UsecaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsecaseService {

    private final UsecaseRepository usecaseRepository;

    @Autowired
    public UsecaseService(UsecaseRepository usecaseRepository) {
        this.usecaseRepository = usecaseRepository;
    }

    public List<Usecase> getAllUsecases() {
        return usecaseRepository.findAll();
    }
}

