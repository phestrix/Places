package ru.phestrix.places.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.phestrix.places.entity.Place
import ru.phestrix.places.util.Coordinates
import ru.phestrix.places.web.PlacesApiClient

@Service
class PlacesService(private val placesApiClient: PlacesApiClient) {
    fun getInterestingPlaces(coordinates: Coordinates): Mono<List<Place>> {
        return placesApiClient.getPlacesByCoordinates(coordinates).map { it.features }
    }
}