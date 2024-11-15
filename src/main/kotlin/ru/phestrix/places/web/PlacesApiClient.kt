package ru.phestrix.places.web

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.phestrix.places.entity.PlacesResponse
import ru.phestrix.places.util.Coordinates

@Component
class PlacesApiClient(@Qualifier("openTripMapWebClient") private val webClient: WebClient, private val apiKey: String) {
    fun getPlacesByCoordinates(coordinates: Coordinates, radius: Int = 1000): Mono<PlacesResponse> {
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/radius")
                    .queryParam("lat", coordinates.latitude)
                    .queryParam("lon", coordinates.longitude)
                    .queryParam("radius", radius)
                    .queryParam("apikey", apiKey)
                    .build()
            }
            .retrieve()
            .bodyToMono(PlacesResponse::class.java)
    }
}