package com.assign.ps.client;

import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

// @ReactiveFeignClient(value = "cinema-service", url = "${cinema.service.url}")
public interface CinemaClient {


    @GetMapping("/cinemas/cities/{id}")
    Mono<String> fetchCinemasByCityId(@PathVariable("id") Integer id);

    @GetMapping("/cinemas/movies/filters")
    Mono<String> filterMovies(@RequestParam("cinemaid") Integer cinemaid);

    @GetMapping("/cinemas/shows/movies/{movieid}")
    Mono<String> fetchShowsByMovieId(@PathVariable("movieid") Integer movieid);

}