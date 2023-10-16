package com.certh.annotationtoolapp.model.post;

import com.mongodb.lang.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
public class UserItem {
    @Nullable
    @Field("username")
    private String username;

    @Getter
    @Field("id")
    private String id;
}
