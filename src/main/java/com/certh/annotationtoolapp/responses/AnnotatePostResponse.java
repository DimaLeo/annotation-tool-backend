package com.certh.annotationtoolapp.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class AnnotatePostResponse {

    @Getter
    private String status;

    @Getter
    private String message;

}
