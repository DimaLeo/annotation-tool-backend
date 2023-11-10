package com.certh.annotationtoolapp.service;

import com.certh.annotationtoolapp.model.user.User;
import com.certh.annotationtoolapp.payload.response.GeneralResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    private final MongoTemplate mongoTemplate;
    BCryptPasswordEncoder encoder;

    public UserService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.encoder = new BCryptPasswordEncoder();
    }

    public GeneralResponse createUser(String username, String password){

        String passwordHash = encoder.encode(password);

        User newUser = new User(username, passwordHash);

        try{
            Query query = new Query(Criteria.where("username").is(username));
            User user = mongoTemplate.findOne(query, User.class, "Users");

            if(user == null){
                mongoTemplate.insert(newUser, "Users");
                return new GeneralResponse("success","Successfully added user to db.");
            }
            else{
                return new GeneralResponse("failed","User already exists");
            }

        }
        catch (Exception e){
            log.error("Error: "+e.getMessage());
            return new GeneralResponse("error","Failed to create user. "+ e.getMessage());
        }
    }

    public User findUserByUsername(String username){
        Query query = new Query(Criteria.where("username").is(username));
        return mongoTemplate.findOne(query, User.class, "Users");
    }
}
