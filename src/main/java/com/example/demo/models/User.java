package com.example.demo.models;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Document(collection ="users")
@Data
public class User {
    @Id

    //private ObjectId_id;

    private  String id;
    @NotBlank
    @Size(max=20)
    private String username;
    @NotBlank
    @Size(max=50)
    private String email;
    @NotBlank
    @Size(max = 120)
    private String password;

    public User( String username, String email,String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public User(){

    }

    @DBRef
    private  Set<Role> roles=new HashSet<>();


}
