package com.platform.catalogue.repository;

import com.platform.catalogue.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, String> {
}