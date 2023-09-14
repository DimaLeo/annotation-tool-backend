package com.certh.annotationtoolapp.requests;

import java.math.BigInteger;

public class FetchPostsRequest {

    private String collectionName;
    private BigInteger lastFetchedPost;

    public String getCollectionName() {
        return collectionName;
    }

    public BigInteger getLastFetchedPost() {
        return lastFetchedPost;
    }
}
