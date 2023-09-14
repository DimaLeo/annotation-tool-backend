package com.certh.annotationtoolapp.service;

import com.certh.annotationtoolapp.model.post.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final MongoTemplate mongoTemplate;

    public PostService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Post> getPostsPaginated(String collectionName, BigInteger lastPostId){
        Integer pageSize = 25;

        Query query = new Query();

        if (lastPostId != null) {
            Criteria skipCriteria = Criteria.where("_id").gt(lastPostId);
            query.addCriteria(skipCriteria);
        } else {
            query.skip(0);
        }

        query.limit(pageSize);

        List<Post> posts;
        posts = mongoTemplate.find(query, Post.class, collectionName);

        return posts;
    }
}
