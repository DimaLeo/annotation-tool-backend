package com.certh.annotationtoolapp.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GeneralResponse {
    String code;
    String message;
}
