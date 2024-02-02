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
import com.assign.ps.client.CinemaWebClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@Slf4j
@RequestMapping(value = "/bookings")
public class BookingController {

    private final CinemaWebClient cinemaWebClient;

    BookingController(CinemaWebClient cinemaWebClient) {
        this.cinemaWebClient = cinemaWebClient;
    }

    @GetMapping("/cities/{id}/cinemas/movies/shows")
    public ResponseEntity<String> browseShowsByCity(@PathVariable String id) {
        log.info("browseCinemas bookings called...{}", id);
        //Get all cinemas in the city
        final Mono<ResponseEntity<String>> browseCinemasByCity = cinemaWebClient.fetchCinemasByCityId(Integer.valueOf(id));
        final ResponseEntity<String> cinemas = browseCinemasByCity.block();
        final Gson gson = new Gson();
        log.debug("fetchCinemasByCityId bookings response cinemaStr ...{}", browseCinemasByCity.block().getBody());
        JsonArray jsonArray = new JsonArray();
        JsonArray cinemaArray = new JsonArray();
        if (cinemas != null) {
            String cinemaStr = cinemas.getBody();
            if(cinemaStr !=null && !cinemaStr.equals("[]")){
            final JsonArray jsonArrayCinemas = gson.fromJson(cinemaStr, JsonArray.class);
            for (JsonElement cinemasEle : jsonArrayCinemas) {
                final String cinemaId = cinemasEle.getAsJsonObject().get("cinemaid").getAsString();
                // Get all movies in the cinemas {cinemaId} of the city {id}
                final Mono<ResponseEntity<String>> movies = cinemaWebClient.filterMovies(Integer.valueOf(cinemaId));
                log.debug("filterMovies for cinemaId {} movies response...{}", cinemaId, movies.block().getBody());
                final JsonElement jsonElementMovies = gson.fromJson(movies.block().getBody(), JsonElement.class);
                final JsonArray jsonArrayMovies = jsonElementMovies.getAsJsonObject().get("items").getAsJsonArray();
                cinemasEle.getAsJsonObject().add("movies", jsonArrayMovies);

                for (JsonElement moviesEle : jsonArrayMovies) {
                    final String movieId = moviesEle.getAsJsonObject().get("movieid").getAsString();
                    //Get all shows for movies in the cinemas of the city
                    final Mono<ResponseEntity<String>> shows = cinemaWebClient.fetchShowsByMovieId(Integer.valueOf(movieId));
                    String show = shows.block().getBody();
                    JsonElement showsElement = JsonParser.parseString(show);
                    log.debug("Got all shows for movieId {} in the cinemaId {} of the city {} , show {}", movieId,cinemaId,id,show);
                    moviesEle.getAsJsonObject().add("shows",showsElement.getAsJsonObject().get("items").getAsJsonArray());

                }
                cinemaArray.add(cinemasEle);
            }
            jsonArray.add(cinemaArray);
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
