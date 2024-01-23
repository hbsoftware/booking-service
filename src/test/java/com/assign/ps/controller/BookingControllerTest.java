package com.assign.ps.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import com.assign.ps.client.CinemaClient;
import com.assign.ps.config.WireMockConfig;
import com.assign.ps.mock.BookingMock;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
@ContextConfiguration(classes = { WireMockConfig.class })
@Slf4j
class BookingControllerTest {

   @Autowired
   CinemaClient client;
   
  @Autowired
  private WireMockServer mockBooksService;

  @Autowired
  private WireMockServer mockMoviesService;

   @BeforeEach
    void setUp() throws IOException {
        BookingMock.setupMockBooksResponse(mockBooksService);
        BookingMock.setupMockBrowseMoviesByCinemaId(mockMoviesService);
        BookingMock.setupMockBrowseShowsByMovieId(mockMoviesService);
    }

	@Test 
	void testCinemasByCity()
	{
		Mono<String> browseShowsByCity = client.fetchCinemasByCityId(1);
        assertNotNull(browseShowsByCity);
        String browseShowsByCityStr = browseShowsByCity.block();
  		log.info("fetchCinemasByCityId received {} {}",mockBooksService.baseUrl(),browseShowsByCityStr);
        Gson gson = new Gson();
        JsonArray browseShowsByCityJson = gson.fromJson(browseShowsByCityStr,JsonArray.class);
        for(JsonElement browseShowsByCityEle:  browseShowsByCityJson){
            String cityId = browseShowsByCityEle.getAsJsonObject().get("cityid").getAsString();
            assertEquals("1",cityId);
            String cinemaId = browseShowsByCityEle.getAsJsonObject().get("cinemaid").getAsString();
            
            Mono<String> browseMoviesByCinemaId = client.filterMovies(1);
            assertNotNull(browseMoviesByCinemaId);
            String browseMoviesByCinemaStr = browseMoviesByCinemaId.block();
            log.info("filterMovies received {} {}",mockBooksService.baseUrl(),browseMoviesByCinemaStr);
            final Mono<String> shows = client.fetchShowsByMovieId(Integer.valueOf(2));
            log.info("fetchShowsByMovieId bookings response shows ...{}",shows.block());

        }    
	}


}
