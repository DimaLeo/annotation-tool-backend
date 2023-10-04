package com.certh.annotationtoolapp.service;

import com.certh.annotationtoolapp.model.post.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
        Criteria inProgressCriteria = Criteria.where("annotation_progress").ne("completed").orOperator(Criteria.where("annotation_progress").isNull());

        query.addCriteria(quoteCriteria);
        query.addCriteria(retweetCriteria);
        query.addCriteria(replyCriteria);
        query.addCriteria(inProgressCriteria);

        if(batchNumber > 1){
            query.skip((long) batchSize * (batchNumber-1));
        }


        List<Post> posts;
        posts = mongoTemplate.find(query, Post.class, collectionName);

        List<Post> updatedPostList = updatePostsWithNewField(posts, "annotation_progress", "in_progress", collectionName);

        return updatedPostList;
    }
}
