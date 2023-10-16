package com.certh.annotationtoolapp.controller;

import com.certh.annotationtoolapp.model.filters.Filters;
import com.certh.annotationtoolapp.model.post.ExtractedLocationItem;
import com.certh.annotationtoolapp.model.post.Post;
import com.certh.annotationtoolapp.requests.AnnotatePostRequest;
import com.certh.annotationtoolapp.requests.GeneralResponse;
import com.certh.annotationtoolapp.requests.ResetProgressRequest;
import com.certh.annotationtoolapp.responses.AnnotatePostResponse;
import com.certh.annotationtoolapp.responses.FetchResponse;
import com.certh.annotationtoolapp.responses.FetchListViewResponse;
import com.certh.annotationtoolapp.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<List<FetchResponse>> getAnnotationPostsBatch(@RequestBody Filters filters){

        List<Post> posts = postService.getAnnotationBatch(filters);
        List<FetchResponse> responseList = new ArrayList<>();

        for(Post post: posts){
            List<String> locationNames = new ArrayList<>();
            for(ExtractedLocationItem item: post.getExtractedLocations()){
                locationNames.add(item.getPlacename());
            }

            responseList.add(new FetchResponse(post.getId(), post.getText(), post.getPlatform(),post.getMediaUrl(), locationNames, post.getTimestamp()));
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

            postService.updatePostBooleanField(requestBody.getId(), "annotated_as", annotatedAs, requestBody.getCollectionName());
            postService.updatePostStringField(requestBody.getId(), "annotation_progress", "completed", requestBody.getCollectionName());

        }

        AnnotatePostResponse responseBody = new AnnotatePostResponse("Success", "Annotation of post successful");

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping("/listview-batch")
    public ResponseEntity<List<FetchListViewResponse>> getListViewPostsBatch(@RequestBody Filters filters){
        List<Post> posts = postService.getListViewBatch(filters);
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

            String relevance = post.getAnnotatedAs()!= null && post.getAnnotatedAs() ? "relevant" : "irrelevant";

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
    public ResponseEntity<String> resetInProgressPostsManual(@PathVariable String collectionName){
        postService.resetProgressFieldManual(collectionName);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/abort-progress")
    public ResponseEntity<GeneralResponse> resetInProgressPosts(@RequestBody ResetProgressRequest requestBody){
        try{
            postService.resetProgressField(requestBody.getCollectionName(), requestBody.getPostIdList());
            return new ResponseEntity<>(new GeneralResponse("Success", "Successfully reverted posts"), HttpStatus.OK);
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            return new ResponseEntity<>(new GeneralResponse("Error", ex.getMessage()), HttpStatus.NOT_MODIFIED);
        }

    }
}
