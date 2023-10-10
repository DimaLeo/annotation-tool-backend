package com.certh.annotationtoolapp.requests;

import lombok.Getter;

import java.util.List;

@Getter
public class ResetProgressRequest {

    private String collectionName;
    private List<String> postIdList;

}
