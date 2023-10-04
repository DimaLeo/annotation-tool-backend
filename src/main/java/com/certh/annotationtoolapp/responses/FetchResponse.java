package com.certh.annotationtoolapp.responses;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.util.List;

public class FetchResponse {
    @Getter
    private String id;
    @Getter
    private String text;
    @Getter
    private String platform;
    @Getter
    private List<String> mediaUrl;
    @Getter
    private List<String> locations;
    @Getter
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String timestamp;

    public FetchResponse(String id, String text, String platform, List<String> mediaUrl, List<String> locations, String timestamp) {
        this.id = id;
        this.text = text;
        this.platform = platform;
        this.mediaUrl = mediaUrl;
        this.locations = locations;
        this.timestamp = timestamp;
    }
}
