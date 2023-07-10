package com.github.oc.mongodbjava8.persistence;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Person {

    @Id
    private String id;

    private String firstName;
    private String lastName;
}
