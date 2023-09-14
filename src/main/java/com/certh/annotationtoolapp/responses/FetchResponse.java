package com.certh.annotationtoolapp.responses;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;

public class FetchResponse {
    private BigInteger mongoId;
    private String text;
    private String platform;
    private String mediaUrl;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String timestamp;
}
