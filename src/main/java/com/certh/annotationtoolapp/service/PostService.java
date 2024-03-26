package com.certh.annotationtoolapp.service;

import com.certh.annotationtoolapp.model.filters.Filters;
import com.certh.annotationtoolapp.model.post.Post;
import com.certh.annotationtoolapp.payload.response.AnnotationCountsResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.List.copyOf;

@Slf4j
@Service
public class PostService {
    private final MongoTemplate mongoTemplate;


    public PostService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void updatePostField(String id, String fieldName, Object fieldValue, String collectionName) {
        log.info("Creating query to update post " + id);
        Query query = new Query(Criteria.where("id").is(id));

        Update update = new Update().set(fieldName, fieldValue);

        log.info("Updating post " + id + " with field: " + fieldName + " and value: " + fieldValue);
        mongoTemplate.updateFirst(query, update, Post.class, collectionName);
    }

    public void updatePostStringField(String id, String fieldName, String fieldValue, String collectionName) {
        updatePostField(id, fieldName, fieldValue, collectionName);
    }

    public void updatePostBooleanField(String id, String fieldName, boolean fieldValue, String collectionName) {
        updatePostField(id, fieldName, fieldValue, collectionName);
    }

    public List<Post> updatePostsWithNewField(List<Post> posts, String fieldName, String fieldValue, String collectionName) {
        log.info("preparing progress update criteria");
        List<Post> resultList = new ArrayList<>();

        log.info("Updating data in db");
        for (Post post : posts) {
            Query query = new Query(Criteria.where("_id").is(post.get_id()));

            Update update = new Update().set(fieldName, fieldValue);
            mongoTemplate.updateFirst(query, update, Post.class, collectionName);
            resultList.add(mongoTemplate.findOne(query, Post.class, collectionName));
        }

        return resultList;

    }

    public List<Post> getListViewBatch(Filters filters) {

        Integer batchSize = 50;

        List<Post> posts = getPostsBatch(filters, "listView", batchSize);
        log.info("Retrieved post batch");

        return posts;
    }

    public List<Post> getAnnotationBatch(Filters filters) {

        Integer batchSize = 15;

        List<Post> posts = getPostsBatch(filters, "annotation", batchSize);

        log.info("Retrieved post batch");
        log.info("Updating posts with annotation progress");

        return updatePostsWithNewField(posts, "annotation_progress", "in_progress", filters.getCollectionName());
    }

    public List<Post> getPostsBatch(Filters filters, String source, Integer batchSize) {


        Query query = generateQuery(filters, source, batchSize);

        if (filters.getBatchNumber() > 1) {
            query.skip((long) batchSize * (filters.getBatchNumber() - 1));
        }

        List<Post> posts;
        log.info("Retrieving data from db");
        posts = mongoTemplate.find(query, Post.class, filters.getCollectionName());

        return posts;
    }

    public Query generateQuery(Filters filters, String source, Integer batchSize) {

        log.info("generating query for " + source);

        Sort sort = Sort.by(Sort.Direction.ASC, "timestamp");
        Query query = new Query().with(sort).limit(batchSize);


        ArrayList<Criteria> criteriaList = new ArrayList<>();
        log.info("Setting static criteria");
        criteriaList.add(Criteria.where("tags.is_quote").ne(true));
        criteriaList.add(Criteria.where("tags.is_retweet").ne(true));

        if (source.equals("annotation")) {
            log.info("setting static criteria for annotation view");
            Criteria inProgressValueCriteria = Criteria.where("annotation_progress").is("aborted");
            Criteria inProgressNullCriteria = Criteria.where("annotation_progress").isNull();
            criteriaList.add(new Criteria().orOperator(inProgressValueCriteria, inProgressNullCriteria));
        }

        if (source.equals("listView")) {

            switch (Objects.requireNonNull(filters.getPostStatus())) {
                case "skipped" -> criteriaList.add(Criteria.where("annotation_progress").is("skipped"));
                case "annotated" -> criteriaList.add(Criteria.where("annotated_as").ne(null));
            }


        }

        log.info("Setting filter criteria");
        if (filters.getHasImage() != null && filters.getHasImage()) {
            criteriaList.add(Criteria.where("media_type").is("image"));
        }
        criteriaList.add(Criteria.where("language").is(filters.getLanguage()));
        criteriaList.add(Criteria.where("timestamp").gte(filters.getFromDate()));
        criteriaList.add(Criteria.where("timestamp").lte(filters.getToDate()));

        Criteria andCriteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));

        query.addCriteria(andCriteria);
        log.info(query.toString());criteriaList.add(Criteria.where("tags.is_quote").ne(true));
criteriaList.add(Criteria.where("tags.is_retweet").ne(true));

        return query;

    }


    public void resetProgressFieldManual(String collectionName) {
        Query query = new Query();

        Criteria inProgressValueCriteria = Criteria.where("annotation_progress").is("in_progress");
        Criteria completedCriteria = Criteria.where("annotation_progress").is("completed");
        Criteria skippedCriteria = Criteria.where("annotation_progress").is("skipped");

        Criteria orCriteria = new Criteria().orOperator(
                inProgressValueCriteria,
                completedCriteria,
                skippedCriteria);

        query.addCriteria(orCriteria);

        Update update = new Update().set("annotation_progress", "aborted");
        Update update2 = new Update().set("annotated_as", null);


        mongoTemplate.updateMulti(query, update, Post.class, collectionName);
        mongoTemplate.updateMulti(query, update2, Post.class, collectionName);
    }

    public void resetProgressField(String collectionName, List<String> postIdList, String status) {

        log.info("Executing resetProgressField");

        Query query = new Query();
        ArrayList<Criteria> orCriteria = new ArrayList<>();

        for (String id : postIdList) {
            orCriteria.add(Criteria.where("id").is(id));
        }

        Criteria finalCriteria = new Criteria().orOperator(orCriteria.toArray(new Criteria[0]));

        query.addCriteria(finalCriteria);

        log.info("Aborting annotation progress of post ids:");
        for (String id : postIdList) {
            log.info(id);
        }
        log.info("due to "+ status);
        Update update = new Update().set("annotation_progress", "aborted");


        mongoTemplate.updateMulti(query, update, Post.class, collectionName);
    }

    public List<Post> getCachedPosts(String collectionName, List<String> postIdList){
        Query query = new Query();
        ArrayList<Criteria> andCriteria = new ArrayList<>();

        andCriteria.add(Criteria.where("id").in(postIdList));
        andCriteria.add(Criteria.where("annotation_progress").is("in_progress"));

        Criteria finalCriteria = new Criteria().andOperator(andCriteria.toArray(new Criteria[0]));

        query.addCriteria(finalCriteria);

        return mongoTemplate.find(query, Post.class, collectionName);

    }

    public AnnotationCountsResponse getAnnotationCounts(Filters filters){

        ArrayList<Criteria> criteriaList = new ArrayList<>();
        log.info("Setting static criteria");
        criteriaList.add(Criteria.where("tags.is_quote").ne(true));
        criteriaList.add(Criteria.where("tags.is_retweet").ne(true));

        if (filters.getHasImage() != null && filters.getHasImage()) {
            criteriaList.add(Criteria.where("media_type").is("image"));
        }

        Query all_posts_number_query = new Query();
        Query annotated_posts_number_query = new Query();
        Query skipped_posts_number_query = new Query();

        criteriaList.add(Criteria.where("language").is(filters.getLanguage()));
        criteriaList.add(Criteria.where("timestamp").gte(filters.getFromDate()));
        criteriaList.add(Criteria.where("timestamp").lte(filters.getToDate()));

        List<Criteria> completedCriteria = new ArrayList<>(copyOf(criteriaList));
        completedCriteria.add(Criteria.where("annotation_progress").is("completed"));

        List<Criteria> skippedCriteria = new ArrayList<>(copyOf(criteriaList));
        skippedCriteria.add(Criteria.where("annotation_progress").is("skipped"));

        Criteria allAndCriteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
        Criteria completedAndCriteria = new Criteria().andOperator(completedCriteria.toArray(new Criteria[0]));
        Criteria skippedAndCriteria = new Criteria().andOperator(skippedCriteria.toArray(new Criteria[0]));

        all_posts_number_query.addCriteria(allAndCriteria);
        annotated_posts_number_query.addCriteria(completedAndCriteria);
        skipped_posts_number_query.addCriteria(skippedAndCriteria);


        System.out.println("Queries:");
        System.out.println(all_posts_number_query.toString());
        System.out.println(annotated_posts_number_query.toString());
        System.out.println(skipped_posts_number_query.toString());

        Long all_posts_number = mongoTemplate.count(all_posts_number_query, Post.class, filters.getCollectionName());
        Long annotated_posts_number = mongoTemplate.count(annotated_posts_number_query, Post.class, filters.getCollectionName());
        Long skipped_posts_number = mongoTemplate.count(skipped_posts_number_query, Post.class, filters.getCollectionName());

        return new AnnotationCountsResponse(all_posts_number, annotated_posts_number, skipped_posts_number);

    }
}
