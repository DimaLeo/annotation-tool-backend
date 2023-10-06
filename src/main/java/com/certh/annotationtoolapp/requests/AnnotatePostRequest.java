package com.certh.annotationtoolapp.requests;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class AnnotatePostRequest {
    private String collectionName;

    private String id;

    private String relevanceInput;
}
