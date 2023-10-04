package com.certh.annotationtoolapp.requests;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;

public class FetchPostsRequest {

    @Getter
    private String collectionName;
    @Getter
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String fromDate;
    @Getter
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String toDate;
    @Getter
    private Integer batchNumber;
}
