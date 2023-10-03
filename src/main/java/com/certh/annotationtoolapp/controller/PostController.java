package com.certh.annotationtoolapp.controller;

import com.certh.annotationtoolapp.model.post.Post;
import com.certh.annotationtoolapp.requests.FetchPostsRequest;
import com.certh.annotationtoolapp.service.PostService;
import com.mongodb.lang.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/annotation-batch")
    public ResponseEntity<List<Post>> getAnnotationPostsBatch(@RequestBody FetchPostsRequest filters){
        List<Post> posts = postService.getPostsBatch(filters.getCollectionName(), filters.getBatchNumber());

        return new ResponseEntity<List<Post>>(posts, HttpStatus.OK);
    }

}
