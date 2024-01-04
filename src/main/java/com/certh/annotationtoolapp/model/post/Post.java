package com.certh.annotationtoolapp.model.post;

import com.certh.annotationtoolapp.model.post.Feature;
import com.certh.annotationtoolapp.model.post.SearchCriteria;
import com.certh.annotationtoolapp.model.post.Tags;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Post {
    @Id
    private BigInteger _id;
    @Field(name = "id")
    private String id;
    @Field(name = "platform")
    private String platform;
    @Field(name = "language")
    private String language;
    @Field(name = "text")
    private String text;
    @Field(name = "media_url")
    private List<String> media_url;
    @Field(name = "media_type")
    private String media_type;
    @Field(name = "timestamp")
    private String timestamp;
    @Field(name = "visual_concepts")
    private List<String> visual_concepts;
    @Field(name = "type")
    private String type;
    @Field(name = "features")
    private List<Feature> features;
    @Field(name = "search_criteria")
    private SearchCriteria searchCriteria;
    @Field(name = "tags")
    private Tags tags;
    @Nullable
    @JsonProperty("annotated_as")
    @Field(name = "annotated_as")
    private Boolean annotatedAs;


    public Post(String id, String platform, String text, List<String> media_url, String media_type, String timestamp, List<String> visual_concepts, String type, List<Feature> features,  String language, SearchCriteria searchCriteria, Tags tags) {
        this.id = id;
        this.platform = platform;
        this.text = text;
        this.media_url = media_url;
        this.media_type = media_type;
        this.timestamp = timestamp;
        this.visual_concepts = visual_concepts;
        this.type = type;
        this.features = features;
        this.language = language;
        this.searchCriteria = searchCriteria;
        this.tags = tags;
    }
}