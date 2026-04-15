package com.platform.catalogue.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "movies")
public class Movie {

    @Id
    private String id;

    private String title;
    private String description;
    private String genre;
    private Integer duration; // minutes
    private String language;

    private LocalDateTime createdAt;
}