package com.certh.annotationtoolapp.responses;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.util.List;

public class FetchListViewResponse {
    @Getter
    private BigInteger mongoId;
    @Getter
    private String id;
    @Getter
    private String text;
    @Getter
    private String platform;
    @Getter
    private List<String> mediaUrl;
    @Getter
    private List<LocationItem> locations;
    @Getter
    private String relevance;
    @Getter
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String timestamp;

    public FetchListViewResponse(BigInteger mongoId, String id, String text, String platform, List<String> mediaUrl, List<LocationItem> locations, String relevance, String timestamp) {
        this.mongoId = mongoId;
        this.id = id;
        this.text = text;
        this.platform = platform;
        this.mediaUrl = mediaUrl;
        this.locations = locations;
        this.relevance = relevance;
        this.timestamp = timestamp;
    }

    // Inner class to represent the detailed location structure specific to the ListView
    public static class LocationItem {
        @Getter
        private String name;
        @Getter
        private Geometry geometry;

        public LocationItem(String name, Geometry geometry) {
            this.name = name;
            this.geometry = geometry;
        }
        
        public static class Geometry {
            @Getter
            private double longitude;
            @Getter
            private double latitude;

            public Geometry(double longitude, double latitude) {
                this.longitude = longitude;
                this.latitude = latitude;
            }
        }
    }
}

