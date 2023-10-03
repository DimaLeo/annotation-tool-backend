package com.certh.annotationtoolapp.requests;

import java.math.BigInteger;

public class FetchPostsRequest {

    private String collectionName;
    private Integer batchNumber;

    public String getCollectionName() {
        return collectionName;
    }

    public Integer getBatchNumber() {
        return batchNumber;
    }
}
