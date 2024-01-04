package com.certh.annotationtoolapp.model.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private ArrayList<String> keywords;
    private ArrayList<String> accounts;

}
