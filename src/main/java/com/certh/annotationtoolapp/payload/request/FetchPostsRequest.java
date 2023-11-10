package com.certh.annotationtoolapp.payload.request;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class FetchPostsRequest {

    private String collectionName;
    private String language;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String fromDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String toDate;
    private Boolean hasImage;
    private Integer batchNumber;
}
