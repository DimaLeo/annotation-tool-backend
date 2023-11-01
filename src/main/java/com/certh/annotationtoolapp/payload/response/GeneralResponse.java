package com.certh.annotationtoolapp.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralResponse {

    private String status;

    private String message;

}
