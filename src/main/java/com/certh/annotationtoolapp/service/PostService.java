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

    public void updatePostField(String id, String fieldName, Object fieldValue, String collectionName) {
        logger.info("Creating query to update post " + id);
        Query query = new Query(Criteria.where("id").is(id));

        Update update = new Update().set(fieldName, fieldValue);

        logger.info("Updating post " + id + " with field: " + fieldName + " and value: " + fieldValue);
        mongoTemplate.updateFirst(query, update, Post.class, collectionName);
    }

    public void updatePostStringField(String id, String fieldName, String fieldValue, String collectionName) {
        updatePostField(id, fieldName, fieldValue, collectionName);
    }

    public void updatePostBooleanField(String id, String fieldName, boolean fieldValue, String collectionName) {
        updatePostField(id, fieldName, fieldValue, collectionName);
    }

    public List<Post> updatePostsWithNewField(List<Post> posts, String fieldName, String fieldValue, String collectionName){
        logger.info("preparing progress update criteria");
        List<Post> resultList = new ArrayList<>();

        logger.info("Updating data in db");
        for(Post post: posts){
            Query query = new Query(Criteria.where("_id").is(post.get_id()));

            Update update = new Update().set(fieldName, fieldValue);
            mongoTemplate.updateFirst(query, update, Post.class, collectionName);
            resultList.add(mongoTemplate.findOne(query, Post.class, collectionName));
        }

        return resultList;

    }

    public List<Post> getPostsBatchTest(String collectionName, Integer batchNumber){
        int batchSize = 25;

        Sort sort = Sort.by(Sort.Direction.ASC, "timestamp");

        Query query = new Query().with(sort).limit(batchSize);

        Criteria quoteCriteria = Criteria.where("is_quote").ne(1);
        Criteria retweetCriteria = Criteria.where("is_retweet").ne(1);
        Criteria replyCriteria = Criteria.where("is_reply").ne(1);
        Criteria andCriteria = new Criteria().andOperator(quoteCriteria, retweetCriteria, replyCriteria);

        query.addCriteria(andCriteria);


        if(batchNumber > 1){
            query.skip((long) batchSize * (batchNumber-1));
        }

        System.out.println(query);

        List<Post> posts;
        posts = mongoTemplate.find(query, Post.class, collectionName);

        return posts;
    }

    public List<Post> getPostsBatch(String collectionName,String selectedLanguage, String fromDate, String toDate, Boolean hasImage, Integer batchNumber){

        logger.info("Executing getPostBatch");

        int batchSize = 5;

        Sort sort = Sort.by(Sort.Direction.ASC, "timestamp");

        Query query = new Query().with(sort).limit(batchSize);

        logger.info("Setting static criteria");
        // Static criteria - predefined
        Criteria quoteCriteria = Criteria.where("is_quote").ne(true);
        Criteria retweetCriteria = Criteria.where("is_retweet").ne(true);
        Criteria replyCriteria = Criteria.where("is_reply").ne(true);
        Criteria inProgressValueCriteria = Criteria.where("annotation_progress").is("aborted");
        Criteria inProgressNullCriteria = Criteria.where("annotation_progress").isNull();
        Criteria orInProgressCriteria = new Criteria().orOperator(inProgressValueCriteria, inProgressNullCriteria);
        logger.info("Setting filter criteria");
        // Filter criteria - defined by ui request
        Criteria mediaUrlCriteria = hasImage? Criteria.where("media_type").is("image"):null;
        Criteria languageCriteria = Criteria.where("language").is(selectedLanguage);
        Criteria fromDateCriteria = Criteria.where("timestamp").gte(fromDate);
        Criteria toDateCriteria = Criteria.where("timestamp").lte(toDate);


        Criteria andCriteria;

        if(hasImage){
            andCriteria = new Criteria().andOperator(
                    quoteCriteria,
                    retweetCriteria,
                    replyCriteria,
                    orInProgressCriteria,
                    mediaUrlCriteria,
                    languageCriteria,
                    fromDateCriteria,
                    toDateCriteria);
        }else{
            andCriteria = new Criteria().andOperator(
                    quoteCriteria,
                    retweetCriteria,
                    replyCriteria,
                    orInProgressCriteria,
                    languageCriteria,
                    fromDateCriteria,
                    toDateCriteria);
        }

        query.addCriteria(andCriteria);


        if(batchNumber > 1){
            query.skip((long) batchSize * (batchNumber-1));
        }

        List<Post> posts;
        logger.info("Retrieving data from db");
        posts = mongoTemplate.find(query, Post.class, collectionName);

        List<Post> updatedPostList = updatePostsWithNewField(posts, "annotation_progress", "in_progress", collectionName);

        return updatedPostList;
    }

    public void resetProgressFieldManual(String collectionName){
        Query query = new Query();

        Criteria inProgressValueCriteria = Criteria.where("annotation_progress").is("in_progress");
        Criteria completedCriteria = Criteria.where("annotation_progress").is("completed");
        Criteria orCriteria = new Criteria().orOperator(
                inProgressValueCriteria,
                completedCriteria);

        query.addCriteria(orCriteria);

        Update update = new Update().set("annotation_progress", "aborted");


        mongoTemplate.updateMulti(query, update, Post.class, collectionName);
    }

    public void resetProgressField(String collectionName, List<String> postIdList){

        logger.info("Executing resetProgressField");

        Query query = new Query();
        ArrayList<Criteria> orCriteria = new ArrayList<>();

        for(String id: postIdList){
            orCriteria.add(Criteria.where("id").is(id));
        }

        Criteria finalCriteria = new Criteria().orOperator(orCriteria.toArray(new Criteria[0]));

        query.addCriteria(finalCriteria);

        logger.info("Aborting annotation progress of post ids:");
        for(String id: postIdList){
            logger.info(id);
        }
        logger.info("due to filter change");
        Update update = new Update().set("annotation_progress", "aborted");


        mongoTemplate.updateMulti(query, update, Post.class, collectionName);
    }
}
