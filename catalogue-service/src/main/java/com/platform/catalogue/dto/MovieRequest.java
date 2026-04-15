package com.platform.catalogue.dto;

import lombok.Data;

@Data
public class MovieRequest {
    private String title;
    private String description;
    private String genre;
    private Integer duration;
    private String language;
}