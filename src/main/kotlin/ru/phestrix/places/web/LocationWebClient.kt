package ru.phestrix.places.web

import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.phestrix.places.entity.GeocodingResponse
import ru.phestrix.places.entity.Location

@Component
class LocationWebClient(private val webClient: WebClient) {
    fun getLocation(query: String): Mono<List<GeocodingResponse>>{
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/geocode")
                    .queryParam("q", query)
                    .build()
            }
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<GeocodingResponse>>() {})
    }
}