package com.certh.annotationtoolapp.model.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.beans.ConstructorProperties;
import java.util.Collection;
import java.util.List;

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