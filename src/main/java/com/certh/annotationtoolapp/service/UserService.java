package com.certh.annotationtoolapp.service;

import com.certh.annotationtoolapp.model.user.User;
import com.certh.annotationtoolapp.responses.GeneralResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
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
            logger.error("Error: "+e.getMessage());
            return new GeneralResponse("error","Failed to create user. "+ e.getMessage());
        }
    }

    public GeneralResponse authenticateUser(String username, String password){
        try{
            Query query = new Query(Criteria.where("username").is(username));
            User user = mongoTemplate.findOne(query, User.class, "Users");

            if(encoder.matches(password, user.getPassword())){
                return new GeneralResponse("success","User successfully authenticated");
            }
            else {
                return new GeneralResponse("unauthorized","Wrong password provided");

            }

        }
        catch (Exception e){
            logger.error("Error: "+e.getMessage());
            return new GeneralResponse("error","Authentication failed"+ e.getMessage());
        }
    }
}
