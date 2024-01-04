package com.certh.annotationtoolapp.model.post;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tags {
    @Nullable
    private Boolean is_retweet;
    @Nullable
    private Boolean is_quote;
}
