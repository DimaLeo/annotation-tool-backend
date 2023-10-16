package com.certh.annotationtoolapp.service;

import com.certh.annotationtoolapp.model.filters.Filters;
import com.certh.annotationtoolapp.model.post.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public List<Post> getListViewBatch(Filters filters){

        Integer batchSize = 50;

        List<Post> posts = getPostsBatch(filters, "listView", batchSize);

        return posts;
    }

    public List<Post> getAnnotationBatch(Filters filters){

        Integer batchSize = 15;

        List<Post> posts = getPostsBatch(filters, "annotation", batchSize);

        List<Post> updatedPostList = updatePostsWithNewField(posts, "annotation_progress", "in_progress", filters.getCollectionName());

        return updatedPostList;
    }
    public List<Post> getPostsBatch(Filters filters, String source, Integer batchSize){


        Query query = generateQuery(filters, source, batchSize);

        if(filters.getBatchNumber() > 1){
            query.skip((long) batchSize * (filters.getBatchNumber()-1));
        }

        List<Post> posts;
        logger.info("Retrieving data from db");
        posts = mongoTemplate.find(query, Post.class, filters.getCollectionName());

        return posts;
    }

    public Query generateQuery(Filters filters, String source, Integer batchSize){

        logger.info("generating query for "+source);

        Sort sort = Sort.by(Sort.Direction.ASC, "timestamp");
        Query query = new Query().with(sort).limit(batchSize);


        ArrayList<Criteria> criteriaList = new ArrayList<>();
        logger.info("Setting static criteria");
        criteriaList.add(Criteria.where("is_quote").ne(true));
        criteriaList.add(Criteria.where("is_retweet").ne(true));
        criteriaList.add(Criteria.where("is_reply").ne(true));

        if(source.equals("annotation")){
            logger.info("setting static criteria for annotation view");
            Criteria inProgressValueCriteria = Criteria.where("annotation_progress").is("aborted");
            Criteria inProgressNullCriteria = Criteria.where("annotation_progress").isNull();
            criteriaList.add(new Criteria().orOperator(inProgressValueCriteria, inProgressNullCriteria));
        }

        logger.info("Setting filter criteria");
        if(filters.getHasImage() != null && filters.getHasImage()){
            criteriaList.add(Criteria.where("media_type").is("image"));
        }
        criteriaList.add(Criteria.where("language").is(filters.getLanguage()));
        criteriaList.add(Criteria.where("timestamp").gte(filters.getFromDate()));
        criteriaList.add(Criteria.where("timestamp").lte(filters.getToDate()));

        Criteria andCriteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));

        query.addCriteria(andCriteria);
        logger.info(query.toString());

        return query;

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
