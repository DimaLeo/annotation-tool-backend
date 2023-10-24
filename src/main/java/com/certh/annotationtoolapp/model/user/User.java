package com.certh.annotationtoolapp.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.beans.ConstructorProperties;

@Getter
@Setter
@Data
@Document(collection = "Users")
public class User {

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Id
    private String id;

    @Field("username")
    @Indexed(unique = true)
    private String username;

    @Field("password")
    private String password;

}
