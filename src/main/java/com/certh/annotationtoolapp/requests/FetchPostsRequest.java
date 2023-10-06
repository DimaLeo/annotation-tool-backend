package com.certh.annotationtoolapp.requests;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;

@Getter
public class FetchPostsRequest {

    private String collectionName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String fromDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String toDate;
    private Integer batchNumber;
}
