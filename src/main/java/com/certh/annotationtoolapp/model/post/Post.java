package com.certh.annotationtoolapp.model.post;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.util.ArrayList;

@Document
@AllArgsConstructor
@Data
public class Post {

    @Id
    private BigInteger _id;
    @Field("id")
    private String id;
    @Field("extracted_locations")
    private ArrayList<ExtractedLocationItem> extractedLocations;
    @Field("is_quote")
    private boolean isQuote;
    @Field("is_reply")
    private boolean isReply;
    @Field("is_retweet")
    private boolean isRetweet;
    @Field("language")
    private String language;
    @Field("matched_keywords")
    private SearchCriteriaItem matchedKeywords;
    @Field("media_type")
    private String mediaType;
    @Field("media_url")
    private ArrayList<String> mediaUrl;
    @Field("mentioned_users")
    private ArrayList<UserItem> mentionedUsers;
    @Field("platform")
    private String platform;
    @Field("possibly_sensitive")
    private Boolean possiblySensitive;
    @Field("quoted_user_id")
    private ArrayList<UserItem> quotedUserId;
    @Field("referenced_id")
    private ArrayList<String> referencedId;
    @Field("replied_user_id")
    private ArrayList<UserItem> repliedUserId;
    @Field("retweeted_user_id")
    private ArrayList<UserItem> retweetedUserId;
    @Field("text")
    private String text;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Field("timestamp")
    private String timestamp;
    @Field("url")
    private String url;
    @Field("userid")
    private String userid;
    @Field("username")
    private String username;
    @Field("username_id")
    private String usernameId;
    @Field("visual_concepts")
    private ArrayList<String> visualConcepts;
    @Field("sentiment")
    @Nullable
    private String sentiment;
    @Field("is_relevant")
    @Nullable
    private Boolean isRelevant;
    @Field("relevance_score")
    @Nullable
    private Double relevanceScore;
    @Field("annotation_progress")
    @Nullable
    @Getter
    @Setter
    private String annotationProgress;

    public String getId() {
        return id;
    }

    public ArrayList<ExtractedLocationItem> getExtractedLocations() {
        return extractedLocations;
    }

    public boolean isIs_quote() {
        return isQuote;
    }

    public boolean isIs_retweet() {
        return isRetweet;
    }

    public String getLanguage() {
        return language;
    }

    public SearchCriteriaItem getMatched_keywords() {
        return matchedKeywords;
    }

    public String getMedia_type() {
        return mediaType;
    }

    public ArrayList<String> getMedia_url() {
        return mediaUrl;
    }

    public String getPlatform() {
        return platform;
    }

    public String getText() {
        return text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Nullable
    public String getSentiment() {
        return sentiment;
    }

    @Nullable
    public Double getRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(@Nullable Double relevanceScore) {
        this.relevanceScore = relevanceScore;
    }


    @Nullable
    public Boolean getRelevant() {
        return isRelevant;
    }

    public void setRelevant(@Nullable Boolean relevant) {
        isRelevant = relevant;
    }}
