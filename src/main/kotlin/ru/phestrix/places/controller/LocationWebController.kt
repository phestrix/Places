package ru.phestrix.places.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import reactor.core.publisher.Mono
import ru.phestrix.places.dto.PlacesDto
import ru.phestrix.places.service.LocationService
import ru.phestrix.places.service.PlaceDetailsService
import ru.phestrix.places.service.PlacesService
import ru.phestrix.places.service.WeatherService
import ru.phestrix.places.util.Coordinates

@Controller
@RequestMapping("/web")
class LocationWebController(
    private val locationService: LocationService,
    private val weatherService: WeatherService,
    private val placesService: PlacesService,
    private val placeDetailsService: PlaceDetailsService
) {

    @GetMapping("/location/info")
    fun getLocationInfo(@RequestParam(required = false) query: String?, model: Model): String {
        if (query.isNullOrBlank()) {
            // Если параметр query отсутствует, возвращаем форму
            return "location-info"
        }
        try {
            val locationsDto = locationService.searchLocations(query).block()
            if (locationsDto?.locations.isNullOrEmpty()) {
                model.addAttribute("error", "No locations found for $query")
            } else {
                model.addAttribute("locations", locationsDto?.locations)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            model.addAttribute("error", e.message ?: "Unknown error")
        }
        return "location-info"
    }

    @GetMapping("location/details")
    fun getLocationDetails(
        @RequestParam lat: Double,
        @RequestParam lng: Double,
        model: Model
    ): String {
        try {
            val coordinates = Coordinates(lat, lng)

            val weatherMono = weatherService.getWeather(coordinates)
                .doOnSubscribe { println("Fetching weather...") }
                .doOnNext { println("Weather fetched: $it") }
                .doOnError { println("Error fetching weather: ${it.message}") }

            val placesMono = placesService.getInterestingPlaces(coordinates, 1000)
                .doOnSubscribe { println("Fetching places...") }
                .doOnNext { println("Places fetched: $it") }
                .doOnError { println("Error fetching places: ${it.message}") }

            val result = Mono.zip(weatherMono, placesMono)
                .doOnSubscribe { println("Zipping weather and places...") }
                .doOnNext { println("Zip result: $it") }
                .flatMap { tuple ->
                    println("Inside flatMap: $tuple")
                    val weather = tuple.t1
                    val places = tuple.t2

                    placeDetailsService.getLimitedPlacesDetails(places)
                        .doOnSubscribe { println("Fetching place details...") }
                        .doOnNext { println("Place details fetched: $it") }
                        .doOnError { println("Error fetching place details: ${it.message}") }
                        .map { detailsList ->
                            println("Mapping details list: $detailsList")
                            PlacesDto(weather, detailsList)
                        }
                }
                .doOnError { error -> println("Error in Mono.zip: ${error.message}") }
                .doOnSuccess { placesDto -> println("Final result: $placesDto") }
                .block()

            println(result)
            if (result != null) {
                model.addAttribute("placesDto", result)
            } else {
                model.addAttribute("error", "Failed to retrieve place details")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            model.addAttribute("error", e.message ?: "Unknown error")
        }

        return "location-info"
    }
}