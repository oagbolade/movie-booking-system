package com.platform.catalogue.service;

import com.platform.catalogue.dto.MovieRequest;
import com.platform.catalogue.dto.MovieResponse;
import com.platform.catalogue.model.Movie;
import com.platform.catalogue.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository repository;

    public MovieResponse create(MovieRequest request) {
        Movie movie = Movie.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .genre(request.getGenre())
                .duration(request.getDuration())
                .language(request.getLanguage())
                .createdAt(LocalDateTime.now())
                .build();

        return toResponse(repository.save(movie));
    }

    public MovieResponse update(String id, MovieRequest request) {
        Movie movie = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setGenre(request.getGenre());
        movie.setDuration(request.getDuration());
        movie.setLanguage(request.getLanguage());

        return toResponse(repository.save(movie));
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public MovieResponse getById(String id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    public Page<MovieResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return repository.findAll(pageable).map(this::toResponse);
    }

    private MovieResponse toResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .genre(movie.getGenre())
                .duration(movie.getDuration())
                .language(movie.getLanguage())
                .build();
    }
}