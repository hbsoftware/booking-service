package com.assign.ps.mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.util.StreamUtils.copyToString;

public class BookingMock {    
    
    public static void setupMockBooksResponse(WireMockServer mockService) throws IOException {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/cinemas/cities/1"))
          .willReturn(
            WireMock.aResponse()
              .withStatus(HttpStatus.OK.value())
              .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
              .withBody(
                copyToString(
                  BookingMock.class.getClassLoader().getResourceAsStream("payload/get-cinemas-cities.json"),
                  defaultCharset()))));
    }

    public static void setupMockBrowseMoviesByCinemaId(WireMockServer mockService) throws IOException{
          mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/cinemas/movies/filters?cinemaid=1"))
          .willReturn(
            WireMock.aResponse()
              .withStatus(HttpStatus.OK.value())
              .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
              .withBody(
                copyToString(
                  BookingMock.class.getClassLoader().getResourceAsStream("payload/get-movies-by-cinema.json"),
                  defaultCharset()))));
    }

    public static void setupMockBrowseShowsByMovieId(WireMockServer mockService) throws IOException{
     
          mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/cinemas/shows/movies/2"))
          .willReturn(
            WireMock.aResponse()
              .withStatus(HttpStatus.OK.value())
              .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
              .withBody(
                copyToString(
                  BookingMock.class.getClassLoader().getResourceAsStream("payload/get-movies-by-cinema.json"),
                  defaultCharset()))));

    }
    

}
