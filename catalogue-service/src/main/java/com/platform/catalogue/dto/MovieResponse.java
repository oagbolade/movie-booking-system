package com.platform.catalogue.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieResponse {
    private String id;
    private String title;
    private String description;
    private String genre;
    private Integer duration;
    private String language;
}