package com.assign.ps.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CinemaWebClient {

   private final WebClient webClient;

   @Value("${cinema.service.url}")
   private String baseCinemaUrl;

    public CinemaWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

 public Mono<ResponseEntity<String>> fetchCinemasByCityId(@PathVariable("id") Integer id){
     log.info("Processing fetchCinemasByCityId for id {}", id);
    String cinemasByCityIdUrl = baseCinemaUrl.concat("/cinemas/cities/").concat(String.valueOf(id));
    return webClient
        .get()
        .uri(cinemasByCityIdUrl)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError
            , clientResponse -> {
              final Mono<ResponseEntity<String>> errorMono = clientResponse.toEntity(String.class);
              return errorMono.flatMap(errorMessage -> {
                log.error("4xxClientError {} occurred for fetchCinemasByCityId for url {} with errormsg {}", clientResponse.statusCode(), cinemasByCityIdUrl,errorMessage.getBody());
                return Mono.error(new ResponseStatusException(clientResponse.statusCode(),
                                                              errorMessage.getBody()));
              });
            })
        .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
          final Mono<ResponseEntity<String>> errorMono = clientResponse.toEntity(String.class);
          return errorMono.flatMap(errorMessage -> {
            log.error("5xxServerError occurred for fetchCinemasByCityId {} for url {} with errormsg {}", clientResponse.statusCode(), cinemasByCityIdUrl,errorMessage.getBody());
            return Mono.error(new ResponseStatusException(clientResponse.statusCode(),
                                                          errorMessage.getBody()));
          });

        }).toEntity(String.class)
        .doOnSuccess(d -> {

        }).doOnError(e -> {

        });
 }

public Mono<ResponseEntity<String>> filterMovies(Integer id) {
    String filterMoviesUrl = baseCinemaUrl.concat("/cinemas/movies/filters?cinemaid="+id);
    log.info("Processing filterMovies with url {} for id {}",filterMoviesUrl, id);

    return webClient
        .get()
        .uri(filterMoviesUrl)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError
            , clientResponse -> {
              final Mono<ResponseEntity<String>> errorMono = clientResponse.toEntity(String.class);
              return errorMono.flatMap(errorMessage -> {
                log.error("4xxClientError {} occurred for filterMoviesUrl for url {} with errormsg {}", clientResponse.statusCode(), filterMoviesUrl,errorMessage.getBody());
                return Mono.error(new ResponseStatusException(clientResponse.statusCode(),
                                                              errorMessage.getBody()));
              });
            })
        .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
          final Mono<ResponseEntity<String>> errorMono = clientResponse.toEntity(String.class);
          return errorMono.flatMap(errorMessage -> {
            log.error("5xxServerError occurred for filterMoviesUrl {} for url {} with errormsg {}", clientResponse.statusCode(), filterMoviesUrl,errorMessage.getBody());
            return Mono.error(new ResponseStatusException(clientResponse.statusCode(),
                                                          errorMessage.getBody()));
          });

        }).toEntity(String.class)
        .doOnSuccess(d -> {

        }).doOnError(e -> {

        });
}

public Mono<ResponseEntity<String>> fetchShowsByMovieId(Integer id) {
    String fetchShowsByMovieIdUrl = baseCinemaUrl.concat("/cinemas/shows/movies/").concat(String.valueOf(id));
    log.info("Processing fetchShowsByMovieIdUrl with url {} for id {}",fetchShowsByMovieIdUrl, id);

    return webClient
        .get()
        .uri(fetchShowsByMovieIdUrl)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError
            , clientResponse -> {
              final Mono<ResponseEntity<String>> errorMono = clientResponse.toEntity(String.class);
              return errorMono.flatMap(errorMessage -> {
                log.error("4xxClientError {} occurred for fetchShowsByMovieIdUrl for url {} with errormsg {}", clientResponse.statusCode(), fetchShowsByMovieIdUrl,errorMessage.getBody());
                return Mono.error(new ResponseStatusException(clientResponse.statusCode(),
                                                              errorMessage.getBody()));
              });
            })
        .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
          final Mono<ResponseEntity<String>> errorMono = clientResponse.toEntity(String.class);
          return errorMono.flatMap(errorMessage -> {
            log.error("5xxServerError occurred for fetchShowsByMovieIdUrl {} for url {} with errormsg {}", clientResponse.statusCode(), fetchShowsByMovieIdUrl,errorMessage.getBody());
            return Mono.error(new ResponseStatusException(clientResponse.statusCode(),
                                                          errorMessage.getBody()));
          });

        }).toEntity(String.class)
        .doOnSuccess(d -> {

        }).doOnError(e -> {

        });
}

}
