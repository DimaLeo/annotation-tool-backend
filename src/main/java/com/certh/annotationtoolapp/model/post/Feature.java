package com.certh.annotationtoolapp.model.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feature {
    private String type;
    private Geometry geometry;
    private Property properties;
}
