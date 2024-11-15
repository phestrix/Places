package ru.phestrix.places.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.phestrix.places.entity.Location
import ru.phestrix.places.entity.Place
import ru.phestrix.places.entity.PlaceDetailsResponse
import ru.phestrix.places.entity.WeatherResponse
import ru.phestrix.places.service.LocationService
import ru.phestrix.places.service.PlaceDetailsService
import ru.phestrix.places.service.PlacesService
import ru.phestrix.places.service.WeatherService
import ru.phestrix.places.util.Coordinates

@RestController
@RequestMapping("/api")
class LocationController(
    private val locationService: LocationService,
    private val weatherService: WeatherService,
    private val placesService: PlacesService,
    private val placeDetailsService: PlaceDetailsService
) {
    @GetMapping("/locations")
    fun searchLocations(@RequestParam query: String): Mono<List<Location>> {
        return locationService.searchLocations(query)
    }

    @GetMapping("/places")
    fun getPlaces(@RequestParam lat: Double, @RequestParam lon: Double,@RequestParam(required = false, defaultValue = "1000") radius: Int): Mono<List<Place>> {
        val coordinates = Coordinates(lat, lon)
        return placesService.getInterestingPlaces(coordinates, radius)
    }

    @GetMapping("/place/details")
    fun getPlaceDetails(@RequestParam xid: String): Mono<PlaceDetailsResponse> {
        return placeDetailsService.getPlaceDetails(xid)
    }

    @GetMapping("/weather")
    fun getWeather(@RequestParam lat: Double, @RequestParam lon: Double): Mono<WeatherResponse> {
        val coordinates = Coordinates(lat, lon)
        return weatherService.getWeather(coordinates)
    }
}