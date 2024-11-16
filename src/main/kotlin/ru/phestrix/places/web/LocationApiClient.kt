package ru.phestrix.places.web

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.phestrix.places.entity.GeocodingResponse
import ru.phestrix.places.web.config.ApiKeyConfig

@Component
class LocationApiClient(@Qualifier("graphhopperWebClient") private val webClient: WebClient, private val apiKeyConfig: ApiKeyConfig) {
    fun getLocation(query: String): Mono<GeocodingResponse>{
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/geocode")
                    .queryParam("q", query)
                    .queryParam("key", apiKeyConfig.graphhopper)
                    .build()
            }
            .retrieve()
            .bodyToMono(GeocodingResponse::class.java)
    }
}