package com.certh.annotationtoolapp.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnnotationCountsResponse {

    private Long total;
    private Long annotated;
    private Long skipped;
}
