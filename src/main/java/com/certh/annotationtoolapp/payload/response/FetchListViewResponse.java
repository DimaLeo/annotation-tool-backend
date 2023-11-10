package com.certh.annotationtoolapp.payload.response;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Getter
public class FetchListViewResponse {
    private String id;
    private String text;
    private String platform;
    private List<String> mediaUrl;
    private List<String> locations;
    private String annotated_as;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String timestamp;

    public FetchListViewResponse(String id, String text, String platform, List<String> mediaUrl, List<String> locations, String annotated_as, String timestamp) {
        this.id = id;
        this.text = text;
        this.platform = platform;
        this.mediaUrl = mediaUrl;
        this.locations = locations;
        this.annotated_as = annotated_as;
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

