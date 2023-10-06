package com.certh.annotationtoolapp.model.post;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class GeometryItem{
    private String type;
    private ArrayList<Float> coordinates;

}
