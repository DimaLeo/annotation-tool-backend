package com.certh.annotationtoolapp.requests;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

public class AnnotatePostRequest {
    @Getter
    private String collectionName;

    @Getter
    private String id;

    @Getter
    private String relevance;
}
