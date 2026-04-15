package com.platform.catalogue.controller;

import com.platform.catalogue.dto.MovieRequest;
import com.platform.catalogue.dto.MovieResponse;
import com.platform.catalogue.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catalogue/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService service;

    @PostMapping
    public MovieResponse create(@RequestBody MovieRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public MovieResponse update(@PathVariable String id,
                                @RequestBody MovieRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public MovieResponse getById(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping
    public Page<MovieResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.getAll(page, size);
    }
}