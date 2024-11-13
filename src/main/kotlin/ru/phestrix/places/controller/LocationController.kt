package ru.phestrix.places.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.phestrix.places.service.LocationService
import ru.phestrix.places.service.WeatherService

@RestController
@RequestMapping("/api")
class LocationController(private val locationService: LocationService, private val weatherService: WeatherService) {
    @GetMapping("/location/{locationId}")
    fun getLocationDetails(@PathVariable locationId: String): Mono<LocationDetailsResponse> {
        return locationService.getLocations(locationId)
            .flatMap { location ->
                val weather = weatherService.getWeather(location.coordinates)
                val places = placesService.getInterestingPlaces(location.coordinates)
                    .flatMapMany { Flux.fromIterable(it) }
                    .flatMap { place -> placeDetailsService.getPlaceDetails(place.id) }
                    .collectList()

                Mono.zip(weather, places) { w, p ->
                    LocationDetailsResponse(location, w, p)
                }
            }
    }
}