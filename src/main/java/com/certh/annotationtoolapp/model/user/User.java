package com.certh.annotationtoolapp.model.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Users")
public class User{

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Id
    private String id;

    @NotBlank
    @Field("username")
    @Indexed(unique = true)
    private String username;

    @NotBlank
    @Field("password")
    private String password;
}
