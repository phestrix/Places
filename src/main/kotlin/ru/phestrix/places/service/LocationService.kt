package ru.phestrix.places.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.phestrix.places.entity.Location
import ru.phestrix.places.web.LocationApiClient

@Service
class LocationService(private val locationApiClient: LocationApiClient) {

    fun searchLocations(query: String): Mono<List<Location>> {
        return locationApiClient.getLocation(query).map { it.hits  }
    }
}