package ru.phestrix.places.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.phestrix.places.entity.Place
import ru.phestrix.places.entity.PlaceDetailsResponse
import ru.phestrix.places.web.PlaceDescriptionApiClient

@Service
class PlaceDetailsService(private val placeDescriptionApiClient: PlaceDescriptionApiClient) {
    fun getPlaceDetails(xid: String): Mono<PlaceDetailsResponse> {
        return placeDescriptionApiClient.getPlaceDetails(xid)
    }
    fun getLimitedPlacesDetails(places: List<Place>): Mono<List<Pair<Place, PlaceDetailsResponse>>> {
        return placeDescriptionApiClient.getLimitedPlacesDetails(places)
    }

}