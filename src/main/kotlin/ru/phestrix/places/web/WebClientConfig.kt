package ru.phestrix.places.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    fun weatherWebClient(): WebClient {
        return WebClient.builder().baseUrl("https://api.openweathermap.org/data/2.5")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }

    @Bean
    fun graphhopperWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("https://graphhopper.com/api/1")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }

    @Bean
    fun openTripMapWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("https://api.opentripmap.com/0.1/en/places")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }
}