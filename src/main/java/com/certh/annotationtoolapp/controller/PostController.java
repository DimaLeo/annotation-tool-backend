package com.certh.annotationtoolapp.controller;

import com.certh.annotationtoolapp.model.post.ExtractedLocationItem;
import com.certh.annotationtoolapp.model.post.GeometryItem;
import com.certh.annotationtoolapp.model.post.Post;
import com.certh.annotationtoolapp.requests.AnnotatePostRequest;
import com.certh.annotationtoolapp.requests.FetchPostsRequest;
import com.certh.annotationtoolapp.responses.AnnotatePostResponse;
import com.certh.annotationtoolapp.responses.FetchResponse;
import com.certh.annotationtoolapp.responses.FetchListViewResponse;
import com.certh.annotationtoolapp.service.PostService;
import com.mongodb.lang.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/annotation-batch")
    public ResponseEntity<List<FetchResponse>> getAnnotationPostsBatch(@RequestBody FetchPostsRequest filters){
        List<Post> posts = postService.getPostsBatch(filters.getCollectionName(), filters.getBatchNumber());
        List<FetchResponse> responseList = new ArrayList<>();

        for(Post post: posts){
            List<String> locationNames = new ArrayList<>();
            for(ExtractedLocationItem item: post.getExtractedLocations()){
                locationNames.add(item.getPlacename());
            }

            responseList.add(new FetchResponse(post.getId(), post.getText(), post.getPlatform(),post.getMediaUrl(), locationNames, post.getTimestamp()));
        }
        for(FetchResponse item: responseList){
            System.out.println(item.getId());
        }


        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @PostMapping("/annotate-post")
    public ResponseEntity<AnnotatePostResponse> annotatePost(@RequestBody AnnotatePostRequest requestBody){

        if(requestBody.getRelevanceInput().equals("skipped")){
            postService.updatePostStringField(requestBody.getId(), "annotation_progress", "completed", requestBody.getCollectionName());
        }
        else{

            boolean annotatedAs;

            annotatedAs = requestBody.getRelevanceInput().equals("relevant");

            postService.updatePostBooleanField(requestBody.getId(), "relevant", annotatedAs, requestBody.getCollectionName());
            postService.updatePostStringField(requestBody.getId(), "annotation_progress", "completed", requestBody.getCollectionName());

        }

        AnnotatePostResponse responseBody = new AnnotatePostResponse("Success", "Annotation of post successful");

        return new ResponseEntity<AnnotatePostResponse>(responseBody, HttpStatus.OK);
    }

    @PostMapping("/listview-batch")
    public ResponseEntity<List<FetchListViewResponse>> getListViewPostsBatch(@RequestBody FetchPostsRequest filters){
        List<Post> posts = postService.getPostsBatch(filters.getCollectionName(), filters.getBatchNumber());
        List<FetchListViewResponse> responseList = new ArrayList<>();

        for (Post post : posts) {
            List<FetchListViewResponse.LocationItem> locationItemList = new ArrayList<>();
            for (ExtractedLocationItem item : post.getExtractedLocations()) {
                ArrayList<Float> coords = item.getGeometry().getCoordinates();
                double longitude = coords.get(0);
                double latitude = coords.get(1);

                FetchListViewResponse.LocationItem.Geometry geometry = 
                    new FetchListViewResponse.LocationItem.Geometry(longitude, latitude);
                locationItemList.add(new FetchListViewResponse.LocationItem(item.getPlacename(), geometry));
            }

            String relevance = post.getRelevant() != null && post.getRelevant() ? "relevant" : "irrelevant";

            FetchListViewResponse response = new FetchListViewResponse(
                post.get_id(),
                post.getId(),
                post.getText(),
                post.getPlatform(),
                post.getMediaUrl(),
                locationItemList,
                relevance, 
                post.getTimestamp()
            );

            responseList.add(response);
        }

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }


    @GetMapping("/reset-in-progress/{collectionName}")
    public ResponseEntity<String> resetInProgressPosts(@PathVariable String collectionName){
        postService.resetProgressField(collectionName);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
