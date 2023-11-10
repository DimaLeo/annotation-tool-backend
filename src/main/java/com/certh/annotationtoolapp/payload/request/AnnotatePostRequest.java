package com.certh.annotationtoolapp.payload.request;

import lombok.Getter;

@Getter
public class AnnotatePostRequest {
    private String collectionName;

    private String id;

    private String relevanceInput;
}
