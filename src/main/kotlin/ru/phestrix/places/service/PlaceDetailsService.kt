package ru.phestrix.places.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.phestrix.places.entity.PlaceDetailsResponse
import ru.phestrix.places.web.PlaceDescriptionClient

@Service
class PlaceDetailsService(private val webClient: PlaceDescriptionClient) {
    fun getPlaceDetails(xid: String): Mono<PlaceDetailsResponse> {
        return webClient.getPlaceDetails(xid)
    }

}