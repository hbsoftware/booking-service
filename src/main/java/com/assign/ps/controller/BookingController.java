package com.assign.ps.controller;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.assign.ps.client.CinemaClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.swagger.v3.oas.annotations.Hidden;

@RestController
@Slf4j
@RequestMapping(value = "/bookings")
public class BookingController {

    private final CinemaClient cinemaClient;

    BookingController(CinemaClient cinemaClient) {
        this.cinemaClient = cinemaClient;
    }

    @GetMapping("/cities/{id}/cinemas/movies/shows")
    public ResponseEntity<String> browseShowsByCity(@PathVariable String id) {
        log.info("browseCinemas bookings called...{}", id);
        //Get all cinemas in the city
        final Mono<String> browseCinemasByCity = cinemaClient.fetchCinemasByCityId(Integer.valueOf(id));
        final String cinemas = browseCinemasByCity.block();
        final Gson gson = new Gson();
        log.info("fetchCinemasByCityId bookings response cinemas ...{}", cinemas);
        JsonArray jsonArray = new JsonArray();
        if (cinemas != null && !cinemas.isEmpty() && !cinemas.equals("[]")) {
            final JsonArray jsonArrayCinemas = gson.fromJson(cinemas, JsonArray.class);
            for (JsonElement cinemasEle : jsonArrayCinemas) {
                final String cinemaId = cinemasEle.getAsJsonObject().get("cinemaid").getAsString();
                //Get all movies in the cinemas {cinemaId} of the city {id}
                final Mono<String> movies = cinemaClient.filterMovies(Integer.valueOf(cinemaId));
                log.info("filterMovies for cinemaId {} movies response...{}", cinemaId, movies.block());
                final JsonElement jsonElementMovies = gson.fromJson(movies.block(), JsonElement.class);
                final JsonArray jsonArrayMovies = jsonElementMovies.getAsJsonObject().get("items").getAsJsonArray();
                for (JsonElement moviesEle : jsonArrayMovies) {
                    final String movieId = moviesEle.getAsJsonObject().get("movieid").getAsString();
                    log.info("Getting all shows for movieId {} in the cinemaId {} of the city {}", movieId,cinemaId,id);
                    //Get all shows for movies in the cinemas of the city
                    final Mono<String> shows = cinemaClient.fetchShowsByMovieId(Integer.valueOf(movieId));
                    String show = shows.block();
                    JsonElement jsonElement = JsonParser.parseString(show);
                    log.info("Got all shows for movieId {} in the cinemaId {} of the city {} , show {}", movieId,cinemaId,id,show);
                    jsonArray.add(jsonElement.getAsJsonObject());
                }
            }
        }
        String json = gson.toJson(jsonArray);
        log.info("Final  all shows return for  {}", json);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @Hidden
    @PostMapping("/book")
    public String bookTickets() {
        log.info("Request received for booking tickets...");
        return "Hi bookings";
    }

}
