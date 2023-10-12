package com.certh.annotationtoolapp.responses;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.util.List;

@Getter
public class FetchResponse {
    private String id;
    private String text;
    private String platform;
    private List<String> mediaUrl;
    private List<String> locations;
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
