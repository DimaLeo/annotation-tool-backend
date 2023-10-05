package com.certh.annotationtoolapp.service;

import com.certh.annotationtoolapp.model.post.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final MongoTemplate mongoTemplate;
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);



    public PostService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void updatePostField(String id, String fieldName, String fieldValue, String collectionName){

        logger.info("Creating query to update post "+id);
        Query query = new Query(Criteria.where("id").is(id));

        Update update = new Update().set(fieldName, fieldValue);

        logger.info("Updating post "+id+" with field: "+fieldName+" and value: "+fieldValue);
        mongoTemplate.updateFirst(query, update, Post.class, collectionName);

    }

    public List<Post> updatePostsWithNewField(List<Post> posts, String fieldName, String fieldValue, String collectionName){

        List<Post> resultList = new ArrayList<>();

        for(Post post: posts){
            Query query = new Query(Criteria.where("_id").is(post.get_id()));

            Update update = new Update().set(fieldName, fieldValue);
            mongoTemplate.updateFirst(query, update, Post.class, collectionName);
            resultList.add(mongoTemplate.findOne(query, Post.class, collectionName));
        }

        return resultList;

    }

    public List<Post> getPostsBatch(String collectionName, Integer batchNumber){
        int batchSize = 5;

        Sort sort = Sort.by(Sort.Direction.ASC, "timestamp");

        Query query = new Query().with(sort).limit(batchSize);

        Criteria quoteCriteria = Criteria.where("is_quote").ne(1);
        Criteria retweetCriteria = Criteria.where("is_retweet").ne(1);
        Criteria replyCriteria = Criteria.where("is_reply").ne(1);
        Criteria inProgressValueCriteria = Criteria.where("annotation_progress").is("aborted");
        Criteria inProgressNullCriteria = Criteria.where("annotation_progress").isNull();
        Criteria orInProgressCriteria = new Criteria().orOperator(inProgressValueCriteria, inProgressNullCriteria);

        Criteria andCriteria = new Criteria().andOperator(quoteCriteria, retweetCriteria, replyCriteria, orInProgressCriteria);

        query.addCriteria(andCriteria);


        if(batchNumber > 1){
            query.skip((long) batchSize * (batchNumber-1));
        }

        System.out.println(query);

        List<Post> posts;
        posts = mongoTemplate.find(query, Post.class, collectionName);

        List<Post> updatedPostList = updatePostsWithNewField(posts, "annotation_progress", "in_progress", collectionName);

        return updatedPostList;
    }

    public void resetProgressField(String collectionName){
        Sort sort = Sort.by(Sort.Direction.ASC, "timestamp");
        Query query = new Query().with(sort);

        Criteria inProgressValueCriteria = Criteria.where("annotation_progress").is("in_progress");
        query.addCriteria(inProgressValueCriteria);

        Update update = new Update().set("annotation_progress", "aborted");


        mongoTemplate.updateMulti(query, update, Post.class, collectionName);
    }
}
