package ru.phestrix.places.web

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.phestrix.places.entity.PlaceDetailsResponse

@Component
class PlaceDescriptionClient(@Qualifier("openTripMapWebClient") private val webClient: WebClient, private val apiKey: String) {
    fun getPlaceDetails(xid: String): Mono<PlaceDetailsResponse> {
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/xid/{xid}")
                    .queryParam("apikey", apiKey)
                    .build(xid)
            }
            .retrieve()
            .bodyToMono(PlaceDetailsResponse::class.java)
    }
}