package com.certh.annotationtoolapp.payload.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ResetProgressRequest {

    private String collectionName;
    private List<String> postIdList;
    private String reason;

}
