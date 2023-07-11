package com.github.oc.msspringmongodb.persistence;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Builder
@Data
public class Person {

    @Id
    private String id;

    private String firstName;
    private String lastName;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    private Instant archivedDate;
}
