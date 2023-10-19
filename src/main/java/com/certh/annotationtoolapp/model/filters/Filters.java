package com.certh.annotationtoolapp.model.filters;

import com.mongodb.lang.Nullable;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class Filters {
    private String collectionName;
    private String language;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String fromDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String toDate;
    private Boolean hasImage;
    private Integer batchNumber;
    @Nullable
    private String postStatus;

}
