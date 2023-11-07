package com.certh.annotationtoolapp.repository;

import com.certh.annotationtoolapp.model.usecase.Usecase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsecaseRepository extends MongoRepository<Usecase, String> {
    
}

