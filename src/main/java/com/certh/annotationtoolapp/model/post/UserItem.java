package com.certh.annotationtoolapp.model.post;

import com.mongodb.lang.Nullable;
import org.springframework.data.mongodb.core.mapping.Field;

public class UserItem {
    @Nullable
    @Field("username")
    private String username;

    @Nullable
    public String getUsername() {
        return username;
    }

    public void setUsername(@Nullable String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Field("id")
    private String id;
}
